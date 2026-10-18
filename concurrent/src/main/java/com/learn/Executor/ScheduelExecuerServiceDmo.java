package com.learn.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduelExecuerServiceDmo {

    public static void main(String[] args){
        int i=0;

        ScheduledExecutorService service = Executors.newScheduledThreadPool(4);

//        System.out.println("cor "+Runtime.getRuntime().availableProcessors());
        service.scheduleAtFixedRate(()-> System.out.println("hello "),2000,500, TimeUnit.MILLISECONDS);

        try {
            if (!service.awaitTermination(5000,TimeUnit.MILLISECONDS)){
            service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}
