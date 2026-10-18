package com.learn.Executor;

import java.util.concurrent.CountDownLatch;

import static java.lang.System.out;

public class Latch {

    public static void main(String[] args){
        CountDownLatch latch =new CountDownLatch(3);
        new Thread(new Chef("A","salad",latch)).start();
        new Thread(new Chef("B","tajin",latch)).start();
        new Thread(new Chef("C","pizza",latch)).start();

        out.println( latch.getCount());

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        out.println("dishes are ready");

    }


}

record Chef(String name, String dish, CountDownLatch latch) implements Runnable {

    @Override
    public void run() {
        try {
            out.println(name + " is preparing " + dish);
            Thread.sleep(2000);
            out.println(name + " has finished preparing " + dish);
            latch.countDown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
