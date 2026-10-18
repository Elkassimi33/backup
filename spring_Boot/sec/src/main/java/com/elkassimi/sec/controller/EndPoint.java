package com.elkassimi.sec.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndPoint{

    @GetMapping
    public String getMessage(){
        return "hello";
    }
}