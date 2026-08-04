package com.shq.demo.pg.controller;

import com.shq.demo.pg.domain.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);


    @RequestMapping("/uploadExcel")
    public ApiResponse uploadExcel() {
        // 定义上传的Excel文件要
        return ApiResponse.success(null);
    }

}
