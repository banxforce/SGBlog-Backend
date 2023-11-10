package org.example.service;

import org.example.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult<Object> uploadImg(MultipartFile img);
}
