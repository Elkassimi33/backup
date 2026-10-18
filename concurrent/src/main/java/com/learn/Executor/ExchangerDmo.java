package com.learn.Executor;


import java.util.concurrent.Exchanger;

public class ExchangerDmo {

    public static void main(String[] args) {
        Exchanger<Integer> exchanger = new Exchanger<>();

        Thread one = new Thread(new FirstThread(exchanger));
        Thread two = new Thread(new SecondThread(exchanger));

        one.start();
        two.start();
    }
}

    class FirstThread implements Runnable{

    private  final Exchanger<Integer> exchanger;

        FirstThread(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            int dataToSend=10;
            System.out.println("First thread is sending data: "+dataToSend);

            try {
                Integer receiveData = exchanger.exchange(dataToSend);
                System.out.println("First thread  received  data: "+receiveData);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    class SecondThread implements Runnable{

    private  final Exchanger<Integer> exchanger;

        SecondThread(Exchanger<Integer> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                int dataToSend=20;
                System.out.println("Second thread is sending data: "+dataToSend);
                Integer receiveData = exchanger.exchange(dataToSend);
                System.out.println("Second thread  received  data: "+receiveData);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }