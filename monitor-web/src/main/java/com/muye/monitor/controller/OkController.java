package com.muye.monitor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OkController {

    @GetMapping("/ok")
    public Object ok(){
        return "ok";
    }
}
