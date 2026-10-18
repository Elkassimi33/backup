package com.webproject.webproject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Dev {

    @Autowired //field injection
    @Qualifier("laptop")
    private Computer comp;

    /*public Dev( Laptop laptop){
        this.laptop=laptop;
    }*/
    /*@Autowired
    public void setLaptop(Laptop laptop){
        this.laptop=laptop;
    }*/
    public void build(){

        comp.compile();
        System.out.println( "hello world, welcome to my home page ");
    }
}
