package com.elkassimi.restaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Class {


    @GetMapping
    public String getMessage(){
        return "Hello";
    }
}
