import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StreamDmo {


    public <T> Set<T> returnDistinctWithOrder(List<T> list) {

        //insertion order change
       return list.stream().collect(Collectors.toSet());
    }
    public <T> List<T> returnDistinctWithoutOrder(List<T> list) {
        return list.stream().distinct().toList();
    }
    public  double findAverage (List<Integer> list){
        return list.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
    public  List<Integer> sortAsn (List<Integer> list){
        return list.stream().distinct().sorted().collect(toList());
    }
    public  List<Integer> sortDesc (List<Integer> list){
        return list.stream().distinct().sorted(Comparator.reverseOrder()).collect(toList());
    }
    public long countStringStartWith (List<String> list,String str){
        return list.stream().distinct().filter(a->a.toLowerCase().startsWith(str.toLowerCase())).count();
    }

    public String joinString (List<String> list){
        return list.stream().collect(Collectors.joining(",","{","}"));
    }
    public boolean isAllNumbersPositive (List<Integer> list){
        return list.stream().allMatch(a->a>0);
    }
    public boolean isAnyNumberDivisibleBy (List<Integer> list,int num){
        return list.stream().anyMatch(a->a%num==0);
    }
    public <T> List<T> flatinList (List<List<T>> lists){
        return lists.stream().flatMap(List::stream).toList();
    }

    public String findFirstNonEmpty (List<String> list){
        return list.stream().filter(a->!a.isBlank()).findFirst().orElse("");
    }

    public Integer findSecondHighestNumber (List<Integer> list){
            return list.stream().sorted(Comparator.reverseOrder()).skip(1).findFirst().orElseThrow(()->new NoSuchElementException("the size of the list is less than 2!"));
    }
}
