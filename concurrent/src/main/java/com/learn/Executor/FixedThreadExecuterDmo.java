package com.learn.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadExecuterDmo {

    public static void main(String[]args) {
        ExecutorService service = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            service.execute(new Task(i));
        }
        service.shutdown();
    }
}
