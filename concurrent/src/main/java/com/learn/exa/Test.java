package com.learn.exa;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;


public class Test {

//    private AtomicBoolean coin = new AtomicBoolean(false);
//    int count =0;
//
//    synchronized int step(){return count++;}
//
//    public void talk(){
//
//        try {
//            Thread.sleep(10000);
//        }catch (InterruptedException e){
//            System.out.println("Interrupted");
//        }
//    }
//    public  void flip(){
//
//        coin.getAndSet(!coin.get());
//    }
//    private Lock lock =new ReentrantLock();
//
//    public void lockUp(){
//
//        if (lock.tryLock()){
//            lock.lock();
//            System.out.println("Locked!");
//            lock.unlock();
//        }
//    }
//
//    long ticketsSold;
//    final AtomicInteger ticketsTaken;
//    public Test(){
//        ticketsSold =0;
//        ticketsTaken = new AtomicInteger(0);
//    }
//    public void  performJob(){
//        IntStream.iterate(1,p->p+1)
//                .parallel()
//                .limit(100)
//                .forEach(i->ticketsTaken.getAndIncrement());
//        IntStream.iterate(1,p->p+1)
//                .parallel()
//                .limit(500)
//                .forEach(i->++ticketsSold);
//
//        System.out.println(ticketsSold+" "+ticketsTaken);
//    }
//
//    private void await(CyclicBarrier c){
//
//        try {
//            c.await();
//        } catch (InterruptedException | BrokenBarrierException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void march(CyclicBarrier c){
//        var s = Executors.newSingleThreadExecutor();
//
//        for (int i = 0; i < 12; i++)
//            s.execute(()->await(c));
//        s.shutdown();
//
//
//    }


    int co=0;
    public synchronized  void  st(){
//        int i=0;
//        while (i<1000000)i++;
//        System.out.println("st");
        co++;
    }
    public   int  getCo(){
        synchronized(Test.class){
//            System.out.println("st1");
            return co;
        }
    }
    public static void main(String[] args)  {
        var test = new Test();

        ExecutorService ex = Executors.newCachedThreadPool();
//        var s=  Executors.newFixedThreadPool(10);
//        for (int i = 0; i < 1000; i++) {
//            s.submit(test::st);
//        }
//
//        s.shutdown();
//        System.out.println(test.getCo());

/**       Thread t = new Thread(test::talk);
        t.start();
        t.interrupt();
        t.interrupt();
        t.interrupt();


        Callable c = new Callable() {
            public Object call() throws Exception {
                System.out.print("X");
                return 10;
            }
        };
        var ex = Executors.newScheduledThreadPool(1);

        for (int i = 0; i < 10; i++) {
            Future f=ex.submit(c);
            f.get();
        }

        ex.shutdown();
        System.out.println("Done");

        for (int i = 0; i < 1000; i++) {
            ex.execute(test::flip);
        }
        ex.shutdown();
        Thread.sleep(5000);
        System.out.println(test.coin.get());

    var counters = new ArrayList<Thread>();

        for (int i = 0; i < 10; i++) {
            counters.add(new Thread(test::step));
        }

        for (Thread counter : counters)
            counter.start();
        counters.forEach(Thread::interrupt);
        Thread.sleep(5000);
        System.out.println(test.count);

        for (int i = 0; i < 5; i++) {
            new Thread(test::lockUp).start();
            Thread.sleep(1000);
        }

    test.performJob();

     var original = List.of(1,2,3,4,5);

     var cp1 = new CopyOnWriteArrayList<Integer>(original);

     for (Integer w:cp1)
            cp1.remove(w);

     var cp2 = Collections.synchronizedList(original);


//     for (Integer w:cp2)
//            cp2.remove(w);

    var cp3 = new ArrayList<Integer>(original);
//
//     for (Integer w:cp3)
//            cp3.remove(w);

        var cp4 = new ConcurrentLinkedDeque<Integer>(original);

        for (Integer w:cp4)
            cp4.remove(w);
 */

ScheduledExecutorService t = Executors.newSingleThreadScheduledExecutor();

//Future f= t.execute(()-> System.out.println("2"));
//
//
//        var db = Collections.synchronizedList(new ArrayList<>())
//;
//        IntStream.range(1,6)
//                .parallel()
//                .map(i->{db.add(i);return i;})
//                .forEachOrdered(System.out::print);
//        System.out.println();
//        db.forEach(System.out::print);

        var original = new ArrayList<Integer>(List.of(1,2,3,4,5));

        var cp1 = new CopyOnWriteArrayList<Integer>(original);

        for (Integer w:cp1)
            cp1.add(1);
//
//        var cp2 = Collections.synchronizedList(original);
//
//
//     for (Integer w:cp2)
//            cp2.add(1);
//
//        var cp3 = new ArrayList<Integer>(original);
//
//     for (Integer w:cp3)
//            cp3.add(1);
//
//        var cp4 = new LinkedBlockingDeque<>(original);
//
//        for (Integer w:cp4)
//            cp4.offer(1);
        double g= 8.8_8_0;

        var lo=LocalDate.of(2022,Month.APRIL,17);
        var ime = LocalTime.of(10,0);
        var zone =ZoneId.of("America/New_York");
        var incr = ZonedDateTime.of(lo,ime,zone);

//
//        var bb= "hhbh \ngfgyh\n uihi  ";
//        System.out.println(bb.length());
//        System.out.println(bb.indent(1));
//        System.out.println(bb.indent(0));
//        System.out.println(bb.indent(-1));
      LocalDate n=   LocalDate.now();
//        System.out.println("dtrdfg\\nio".translateEscapes());

       var date = LocalDate.of(22,Month.MARCH,3);
       var date2 =date.plusDays(2).minusDays(1).minusDays(1);


//       Boolean.valueOf("8").booleanValue();
//
//        System.out.println(Double.valueOf("97.88").byteValue());
//        System.out.println(Long.valueOf(128).byteValue());


    }
}
