package com.spring.youssef;

public class Laptop implements Computer {

    public Laptop(){
        System.out.println("Laptop constructor");
    }

    @Override
    public void compile(){
        System.out.println("compiling in laptop");
    }


}
