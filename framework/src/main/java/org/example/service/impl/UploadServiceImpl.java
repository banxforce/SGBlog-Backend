package org.example.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Setter;
import org.example.domain.ResponseResult;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.UploadService;
import org.example.utils.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@ConfigurationProperties(prefix = "oss")
@Setter
public class UploadServiceImpl implements UploadService {

    // 会通过setter从配置文件中获取
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String cdn;

    /**
     * 将上传的JPG/JPEG/PNG上传至OSS
     * @param img 要上传的文件
     * @return ResponseResult data:上传后的图片URL
     */
    @Override
    public ResponseResult<Object> uploadImg(MultipartFile img) {
        // 判断文件格式
        String originalFilename = img.getOriginalFilename();
        if(!originalFilename.endsWith(".jpg") && !originalFilename.endsWith(".jpeg") && !originalFilename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //上传文件
        String fileName = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img, fileName);
        //返回
        return ResponseResult.okResult(url);
    }

    private String uploadOss(MultipartFile img,String fileName) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传


        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;

        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
                return "http://" + cdn + putRet.key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
