package exam.collection;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class c{
}
class b extends c  {
}
class a{
}

public class Co  {
    public static void trow(Collection<? extends RuntimeException> coll){

        var it = coll.iterator();
        if (it.hasNext())
            throw it.next();

    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(1);


    }


}
