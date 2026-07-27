package com.shq.demo.pg.controller;

import com.shq.demo.pg.mapper.PgInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pg")
public class PgTestController {

    @Autowired
    PgInfoMapper pgInfoMapper;

    @GetMapping("/version")
    public Map<String, Object> getVersion() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("version", pgInfoMapper.getVersion());
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

}
