package org.example;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@SpringBootTest
//@ConfigurationProperties(prefix = "oss")
public class OSSTest {

    /**
     *设置AccessKey和SecretKey；
     *创建Mac对象；
     *创建PutPolicy对象；
     *生成UploadToken；
     *创建PutExtra对象；
     *调用put或putFile方法上传文件；
     */
    //        Config.ACCESS_KEY = "5ioHm8igPD33iL9P2kEe0WS-I6e5kvEtKWhHOyK-";
//        Config.SECRET_KEY = "MJ1KMRRIMyKdeh2egoAu8jq2UEP9p2GYvE7c8T1j";
    @Test
    public void test() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "5ioHm8igPD33iL9P2kEe0WS-I6e5kvEtKWhHOyK-";
        String secretKey = "MJ1KMRRIMyKdeh2egoAu8jq2UEP9p2GYvE7c8T1j";
        String bucket = "sg-blog-mrw";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = new Date().toString() + "/" + "白丝.jpg";

        try {
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            FileInputStream inputStream = new FileInputStream("D:\\图片\\倒影.jpg");
            //ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
