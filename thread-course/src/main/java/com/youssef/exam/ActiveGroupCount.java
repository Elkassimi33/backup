package com.youssef.exam;

import java.io.IOError;

class ThreadNew extends Thread {

    public ThreadNew(String tName,ThreadGroup tgrp){
        super(tgrp,tName);
        start();
    }

    public void run(){

        for(int i=0;i<=100;i++){
            try {
                Thread.sleep(5);
            }catch (InterruptedException e){
                System.out.println("The exception has been encountered. "+e);
            }
        }
        System.out.println(Thread.currentThread().getName()+" thread has finished executing.");
    }
}

public  class ActiveGroupCount{
     public static void main(String[] args) throws InterruptedException {
         ThreadGroup tg1 =new ThreadGroup("Parent thread group");
         ThreadGroup tg2 =new ThreadGroup(tg1,"Child thread group");

         ThreadNew th1 =new ThreadNew("the first thread",tg1);
         System.out.println("starting the first thread");

         ThreadNew th2 =new ThreadNew("the second thread",tg1);
         System.out.println("starting the second thread");

         Thread[] grp = new Thread[tg1.activeCount()];

         int cnt =tg1.enumerate(grp);

         for (int i=0;i<cnt;i++) {
             System.out.println("Thread " + grp[i].getName() + " is found.");
             System.out.println(grp[i].getThreadGroup().getParent());
         }


//         th1.join();
//         th2.join();
//
//
//         System.out.println("Totale number of active threads: "+tg1.activeCount());
     }
}
