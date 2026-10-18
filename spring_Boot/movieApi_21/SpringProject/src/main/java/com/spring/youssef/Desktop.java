package com.spring.youssef;

public class Desktop implements Computer {

    public Desktop(){

        System.out.println("Desktop constructor");
    }

    @Override
    public void compile(){

        System.out.println("compiling in Desktop");
    }
}
