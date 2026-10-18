package com.learn.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueDmo {

    private static final int QUEUE_CAPACITY=10;

    static BlockingQueue<Integer> tasks = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    public static void main(String[] args){

        Thread producer = new Thread(()->{
            try {
                for (int i = 0; i <=20; i++) {

                    tasks.put(i);
                    System.out.println("task Produced: "+i);
                    Thread.sleep(100);

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumerOne = new Thread(()->{
            try {
                while (true) {
                    int task= tasks.take();
                    processTask(task,"consumerOne");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread consumerTow = new Thread(()->{
            try {
                while (true) {
                    int task= tasks.take();
                    processTask(task,"consumerTow");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumerOne.start();
        consumerTow.start();


        try {
            producer.join();
            consumerOne.join();
            consumerTow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processTask(int task,String consumerName)throws InterruptedException{
        System.out.println("task being processed by: "+consumerName+" : "+task);
        Thread.sleep(1000);
        System.out.println("Task consumed by: "+consumerName+" : "+task);
    }
}
