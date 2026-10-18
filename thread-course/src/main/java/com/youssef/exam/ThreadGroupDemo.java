package com.youssef.exam;

public class ThreadGroupDemo implements Runnable {

    public void run(){
        System.out.println(Thread.currentThread().getName());
    }
    public static void main(String[] args) {

        Runnable rn = new ThreadGroupDemo();
        ThreadGroup tg1 =new ThreadGroup("Parent thread group");
        Thread t1=new Thread(tg1,rn,"one");
        t1.start();
        Thread t2=new Thread(tg1,rn,"two");
        t2.start();
        Thread t3=new Thread(tg1,rn,"three");
        t3.start();

        System.out.println("Thread group name:"+tg1.getName());

        tg1.list();

    }
}