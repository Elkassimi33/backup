package function_interface;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestExa {

    public void min(BiFunction<Integer,Double,Double> lambda){

        System.out.println(lambda.apply(2,3.4));
    }

    public static void main(String[] args){

//        var ods = IntStream.iterate(1,a->a+2);
//        var evens = IntStream.iterate(2,a->a+2);
//
//        var sum = IntStream.concat(ods,evens).limit(3).sum();
//        System.out.println(sum);
//
//        var empty = Optional.empty();
//        var pram = Optional.of(null);
//        var method = Optional.ofNullable(null);
//
//        ToIntFunction<Integer> transformer = x->x;
//
//        var prim = Stream.of(3,1,4,1,5,9)
//                .limit(1)
//                .peek(s->{})
//                .mapToInt(transformer)
//                .peek(s->{})
//                .sum();
//        System.out.println(prim);
//
//        System.out.println(Stream.of(3,1,4,1,5,9).min(Integer::compareTo));
//        new TestExa().min((s,p)->s+p);
//        Stream.of(List.of(1,2,1,1)).map(x->x.size()>6).forEach(System.out::println);
//        ToDoubleBiFunction<Integer,Double> f=(a,b)->{int v; return b;};
//        f.applyAsDouble(1,22.2);
//
//        Function<Integer,ArrayList<Integer>> r = ArrayList::new;
//
//        var l =new LinkedList<Integer>();
//        l.add(1);
//        l.add(2);
//
//        Stream s =l.stream();
//
//        s.forEach(System.out::println);
//        s.forEach(System.out::println);
//
//        Predicate<String> dash =c->c.startsWith("_");
//        dash.test("_");
//        Consumer clear = System.out::println;
//        clear.accept("uuu");
//
//        Comparator<String> c = (String n,String t)->0;
//        System.out.println(c.compare("q","s"));
//
//        Optional<Boolean> op = Optional.of(true);
//         op.map(x -> "run " + x).ifPresent(System.out::println);

         Stream.iterate(1,x->x+1)
                 .limit(5)
                 .skip(2)
                 .peek(System.out::print)
                 .collect(Collectors.toList())
                 .forEach(System.out::print);
    }
}
