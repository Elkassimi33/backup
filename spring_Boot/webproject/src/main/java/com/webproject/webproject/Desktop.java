package com.webproject.webproject;

import org.springframework.stereotype.Component;

@Component
public class Desktop implements Computer {
    @Override
    public void compile() {
        System.out.println("compiling in Desktop");
    }
}
