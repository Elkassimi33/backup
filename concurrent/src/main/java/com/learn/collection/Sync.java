package com.learn.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Sync {

    public static void main(String[]args){
        List<Integer> l = Collections.synchronizedList(new ArrayList<>());

        var t =new Thread(()->{
            Stream.iterate(0, i -> i < 1000, i -> (Integer) (i + 1)).forEach(l::add);
        });

        var t2 = new Thread(()->{
            Stream.iterate(0, i -> i < 1000, i -> (Integer) (i + 1)).forEach(l::add);
        });
        t.start();
        t2.start();

        try {
            t.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(l.size());
    }
}
