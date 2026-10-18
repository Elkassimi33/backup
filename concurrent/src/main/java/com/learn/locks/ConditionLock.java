package com.learn.locks;

import javax.net.ssl.ManagerFactoryParameters;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionLock {
    public  final Integer MAX_SIZE = 5;
    private final Lock lock = new ReentrantLock();
    private final Queue<Integer> buffer = new LinkedList<>();
    private final Condition bufferNotFull = lock.newCondition();
    private final Condition bufferNotEmpty = lock.newCondition();


    private void produce(int item) throws InterruptedException{
        lock.lock();
        try {
            while (buffer.size()==MAX_SIZE){
                bufferNotEmpty.await();
            }
            buffer.offer(item);

            System.out.println("Produced >> "+item);
            bufferNotFull.signal();
        }
        finally {
            lock.unlock();
        }
    }
    private void consumer() throws InterruptedException{

        lock.lock();
        try {
            while (buffer.isEmpty()){
                bufferNotFull.await();
            }
            System.out.println("Consumed << "+ buffer.poll());
            bufferNotEmpty.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionLock conditionLock = new ConditionLock();

        Thread producer = new Thread(()->{
            try {
                for (int i = 0; i < 10; i++) {
                    conditionLock.produce(i);
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }

        });
        Thread consumer = new Thread(()->{
            try {
                for (int i = 0; i < 10; i++) {
                    conditionLock.consumer();
                    Thread.sleep(7000);
                }
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumer.start();


    }
}
