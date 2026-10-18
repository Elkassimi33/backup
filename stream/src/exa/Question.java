package exa;

import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Question {

    public  static void  main(String[] args){
//
//        var ints = IntStream.of(6,10);
//        var longs = ints.mapToLong(i->i);
//        var first = longs.average().getAsDouble();
//
//        var morLongs = LongStream.of(6,10);
//        var stats = morLongs.summaryStatistics();
//        var second = stats.getAverage();
//
//        System.out.println("*"+ first +"-"+second+"*");
//        List.of(1,2,3,4)
//                .parallelStream()
//                .parallel()
//                .parallel()
//                .parallel()
//                .forEach(System.out::println);

        record ball(String a,int b,int c){}

//        Stream<ball> s =Stream.of(
//                new ball("a",1,2),
//                new ball("b",2,4),
//                new ball("c",3,5),
//                new ball("d",4,5)
//        );
//
//        var score = s.collect(
//                Collectors.groupingBy(ball::a),
//                Collectors.summingInt(ball::b),
//
//        );
//        );
//        );


    }
}
