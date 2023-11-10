package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "上传")
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @Operation(description = "上传文章缩略图")
    // 对MultipartFile打印入参会出现错误
    //@SystemLog(businessName = "上传文章缩略图")
    @PostMapping
    public ResponseResult<Object> uploadImg(@RequestParam MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
