package classT;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ClassT {

    public static <T>  void printClass(Class<T> c){
        Map<Integer, List<Class<?>>> parametres = new HashMap<>();
        int keys =0 ;

       for (Constructor<T> constructor : (Constructor<T>[]) c.getDeclaredConstructors()) {
//         parametres.put(keys++, Arrays.stream(constructor.getParameters()).map(p->p.getType()).toList());
       }

        System.out.println(parametres);
    }

    public static void main(String[] args) throws Exception {

        printClass(Person.class);
    }
}
