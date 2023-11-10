package org.example.controller;

import jakarta.annotation.Resource;
import org.example.annotation.SystemLog;
import org.example.domain.ResponseResult;
import org.example.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    @SystemLog(businessName = "获取友链")
    @GetMapping("/getAllLink")
    public ResponseResult<Object> getAllLink(){
        return linkService.getAllLink();
    }

}
