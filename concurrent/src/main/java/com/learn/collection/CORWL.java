package com.learn.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CORWL {

    public static void main(String[] args) {
        Simulation simulation = new Simulation();

        simulation.simulate();
    }
}


class Simulation{
    public  final  List<Integer> list;

    Simulation() {
        this.list = new CopyOnWriteArrayList<>();
        this.list.addAll(Arrays.asList(0,0,0,0,0,0,0,0,0));
    }

    public void simulate(){
        Thread one = new Thread(new WriteTask(list));
        Thread two = new Thread(new WriteTask(list));
        Thread tree = new Thread(new WriteTask(list));
        Thread four = new Thread(new ReadTask(list));

        one.start();
        two.start();
        tree.start();
        four.start();
    }
}
class WriteTask implements Runnable{
    private  final List<Integer> list;
    private final Random random;

    WriteTask(List<Integer> list) {
        this.list = list;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            list.set(random.nextInt(list.size()), random.nextInt(10));
        }
    }
}
class ReadTask implements Runnable{
    private  final List<Integer> list;

    ReadTask(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                System.out.println(list);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}