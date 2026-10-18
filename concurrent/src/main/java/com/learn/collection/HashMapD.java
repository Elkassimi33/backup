package com.learn.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapD {

    private static final Map<String,String> cache = new ConcurrentHashMap<>();

    public static void main(String[] args){

        for (int i = 0; i < 10; i++) {
            final  int treadNum =i;
            new Thread(()->{
                String key ="key @ "+treadNum;
                for (int j = 0; j < 3; j++) {
                    String value = getCacheValue(key);
                    System.out.println("Thread: " + Thread.currentThread().getName() + " key: " + key + " value: " + value);
                }
            }).start();
        }
    }

    private static String getCacheValue(final String key) {
        cache.computeIfPresent(key,(k,v)->cache.get(key)+"  ss");
        return cache.computeIfAbsent(key,HashMapD::compute);

    }

    private static String compute(final String key){

        System.out.println(key+" not present in the cache, so going to compute");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return key+" computed value";
    }
}
