package com.shq.demo.base64.controller;


import com.shq.demo.base64.service.PhotoExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    @Autowired
    private PhotoExtractService photoExtractService;

    @GetMapping("/extract")
    public String extract() {
        photoExtractService.extractPhotos();
        return "照片解析任务已执行完成，请查看输出目录";
    }
}