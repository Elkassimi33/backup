package com.learn.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecuterDmo {

    public static void main(String[]args) {
        ExecutorService service = Executors.newSingleThreadExecutor();

            for (int i = 0; i < 5; i++) {
                service.execute(new Task(i));
            }
            service.shutdown();
    }

}

 record Task(int id) implements Runnable {
    @Override
    public void run() {
        System.out.println("Task with id: "+id+ " being executed by: "+Thread.currentThread().getName());

        try {
            Thread.sleep(500);
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }
    }
}
