package com.yoti.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping(path = "/test")
    public String getTestData(){
        log.debug("Tesing mike testing!!!");
        return "Justin";
    }
}
