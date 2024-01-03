package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "UploadController", description = "上传相关接口")
@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

    //@SystemLog(businessName = "上传头像")
    @PostMapping("/upload")
    public ResponseResult<Object> uploadImg( MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
