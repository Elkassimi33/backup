package function_interface;


import jdk.dynalink.Operation;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FunctionInterface {


    public static void main(String[] args){
//        Function<Integer,String> function= i-> Objects.toString(i * 2,"");
//        BiConsumer<String,Integer> print=(s,i)-> System.out.println(s.charAt(i));
//        BiConsumer<String,Integer> validate=(s,i)-> {
//            if (i>s.length()) throw new  IllegalArgumentException("length< "+i);
//        };
//        BiConsumer<String,Integer> validateAndPrint =validate.andThen(print);
//        validateAndPrint.accept("abc",4);

//        ==========CONSUMER==========

/*        BiConsumer<StringBuilder,String> append = StringBuilder::append;
        BiConsumer<StringBuilder,String> addComma = (sb,s)->sb.append(", ");

        DoubleConsumer doubleConsumer = System.out::println;

        BiConsumer<StringBuilder,String> appendAndAddComma=append.andThen(addComma);
        StringBuilder sb=new StringBuilder();
        appendAndAddComma.accept(sb,"youssef");
        appendAndAddComma.accept(sb,"amine");
        appendAndAddComma.accept(sb,"youness");
        System.out.println(sb.substring(0,sb.length()-2));

        ObjDoubleConsumer<String > objDoubleConsumer = (s,d)-> System.out.println(s+d);

 */

//        ==========FUNCTION==========
/*        Function<Integer,Character> mapToChar=i-> (char) i.intValue();
        Function<String, List<Character>> function= (s)-> s.chars().boxed().map(mapToChar).toList();
        Function<String, List<Character>> function1= (s)-> s.chars().mapToObj(i->(char) i).toList();
        System.out.println(function.apply("abc"));

        Function<Integer,String> convertToString = Objects::toString;
        Function<Double,Integer> doubleToInteger = Double::intValue;
        Function<Double,String> doubleToString = doubleToInteger.andThen(convertToString);
        Function<Double,String> doubleToString1 = convertToString.compose(doubleToInteger);
        Function<Double,Double> identity =Function.identity();
        System.out.println(doubleToString1.apply(7.8d));
        System.out.println(doubleToString.apply(7.7d));
        System.out.println(identity.apply(3456.99d));

        BinaryOperator<String> binaryOperator
*/

//        ==========BinaryOperator==========

/*        Comparator<Integer> comparator =Integer::compareTo;

        BinaryOperator<Integer> minInteger = BinaryOperator.minBy(comparator);
        BinaryOperator<Integer> maxInteger = BinaryOperator.maxBy(comparator);

        System.out.println(maxInteger.apply(28,299)+" "+minInteger.apply(28,299));
*/

        Predicate<Integer> integerPredicate = integer -> integer>0;
        Predicate<Integer> integerPredicate1= integerPredicate.negate();

        Supplier<Integer> randInt=()-> new Random().nextInt(100);

        int[] rand100 = IntStream.range(0,100)
                .map(i-> randInt.get())
                .toArray();
        System.out.println(Arrays.toString(rand100));



    }
}
