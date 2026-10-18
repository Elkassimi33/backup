package com.publicM;

import com.privatepack.ClassA;

public class ClassB {
    private final String name;

    public ClassB(String name) {
        this.name = name;
    }

    public void talk(String name){

        var classA = new ClassA(name);
        System.out.println("ClassA will talk from classB talk method");
        classA.talk(name);
    }
}
