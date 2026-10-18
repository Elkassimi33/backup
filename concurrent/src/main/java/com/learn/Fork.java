package com.learn;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveTask;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class Fork extends RecursiveTask<Integer> {

    private final int[] arr;

    public Fork(int[] arr){
        this.arr=arr;
    }


    @Override
    protected Integer compute() {
        if(arr.length>5000){
           int mid = arr.length/2;
           Fork task1 = new Fork(Arrays.copyOfRange(arr,0,mid));
           Fork task2 = new Fork(Arrays.copyOfRange(arr,mid,arr.length));

           task1.fork();
           task2.fork();

           return  task1.join()+task2.join();
        }else {
           return Arrays.stream(arr).sum();
        }
    }

    public static void main(String[] args) {
        IntSupplier rand = ()->new Random().nextInt(100);
        int[] arr = IntStream.generate(rand)
                .limit(100000000)
                .toArray();
//        int sum = Arrays.stream(arr).sum();
        int sum = new Fork(arr).invoke();
//
        System.out.println(sum);
    }
}
