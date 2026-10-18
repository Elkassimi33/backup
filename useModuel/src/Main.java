import com.module.Nice;
//import com.privatepack.ClassA;
import com.publicM.ClassB;


public class Main {
    public static void main(String[] args) {
        Nice nice = new Nice();
//        ClassA classA =new ClassA("jjja");
        ClassB classb =new ClassB("jjja");

        classb.talk("jsjj");

//        classA.talk("jdjjd");
    }
}