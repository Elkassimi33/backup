import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
//        Stream str=new Stream();
//        List<Integer> numbers = Arrays.asList( 5,1,3,2,5,32,13,5);
//        List<List<Integer>> listOfList=Arrays.asList(Arrays.asList(1,2,3),Arrays.asList(4,5,6),Arrays.asList(7,8,9));
//        List<String> fruit=Arrays.asList("apple","orange","lemon","banana","grapefruit","avocado","Mango","Apricot");
//        List<String> fruits=Arrays.asList("","","lemon","banana","grapefruit","avocado","Mango","Apricot");
//
//        System.out.println("without order \n"+str.returnDistinctWithoutOrder(numbers));
//        System.out.println("with order \n"+str.returnDistinctWithOrder(numbers));
//        System.out.println("find average \n"+str.findAverage(numbers));
//        System.out.println("sorted list asc \n"+str.sortAsn(numbers));
//        System.out.println("sorted list desc \n"+str.sortDesc(numbers));
//        System.out.println("the number of string start with a is: "+str.countStringStartWith(fruit,"A"));
//        System.out.println("join all string in one and separate them with coma result: "+str.joinString(fruit));
//        System.out.println("is all numbers positive : "+str.isAllNumbersPositive(numbers));
//        System.out.println("is any number is divisible by 3: "+str.isAnyNumberDivisibleBy(numbers,3));
//        System.out.println("the flat list is: "+str.flatinList(listOfList));
//        System.out.println("the first non empty string is: "+str.findFirstNonEmpty(fruits));
//        System.out.println("the second highest number in the list is: "+str.findSecondHighestNumber(numbers));

        List<Integer> nbr= IntStream.range(1,100)
                .boxed()
                .toList();
        List<Integer> even = Stream.iterate(0,n->n <=100,n->n+2)
                .toList();

        List<Integer> fib= Stream.iterate(new int[]{0,1},arr->new int[]{arr[1],arr[0]+arr[1]})
                .limit(10)
//                .peek(i->{
//                    System.out.println(i[0]+" "+i[1]);
//                })
                .map(arr->arr[0])
                .toList();
        System.out.println(fib);

    }
}