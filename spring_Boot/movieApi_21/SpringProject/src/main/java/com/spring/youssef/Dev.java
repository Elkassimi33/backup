package com.spring.youssef;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Dev {

//     private int  age;
    private Computer com;

    public Dev(){
        System.out.println("Dev constructor");
  }
    public void build(){

        com.compile();
        System.out.println("we almost finish building");}

    public Computer getCom() {
        return com;
    }

    public void setCom(Computer com) {
        this.com = com;
    }
}



