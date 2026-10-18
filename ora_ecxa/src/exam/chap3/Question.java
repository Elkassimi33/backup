package exam.chap3;


import java.io.IOException;

interface   Story {
     String g="jj";
         int send() ;
}

abstract  class dino implements Story {
    public       int send(){System.out.println("eee");return 8;}
}
interface Int{
    default int send() {return 7;}
}
public class Question implements AutoCloseable   /*implements Story,Int*/  {

    int b;
    public Question(int n)  {
        b=n;
    }

    public    int send() {
        return 0;
    }

    public static  void main(String... args) throws Exception  {
         var  question = new Question(6);

//         System.out.println(question.send());
        try (question; Question h=new Question(7);){
            throw new  Exception();
//            System.out.println("kk");
        }catch (Throwable ignored){

        }
        finally {
            System.out.println("9");
        }


    }


    @Override
    public void close() throws RuntimeException {
        new RuntimeException("jm");

        System.out.println(b);
    }
}