package com.learn.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockD {
    private  final ReentrantLock  lock = new ReentrantLock();
    private int sharedData=0;

    private void incr(){
        lock.lock();
        try {
            sharedData++;
            System.out.println("Methode incr sharedData= "+sharedData);
            decr();
        }finally {
            lock.unlock();
        }
    }
    private void decr(){
        lock.lock();
        try {
            sharedData--;
            System.out.println("Methode decr sharedData= "+sharedData);
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockD dmo =  new ReentrantLockD();

        for (int i = 0; i < 6; i++) {
            new Thread(dmo::incr).start();
        }


    }
}
