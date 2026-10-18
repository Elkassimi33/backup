package exa;

import java.util.function.UnaryOperator;

public class Doll {

    int layer;

    public Doll(int layer){
        this.layer=layer;
    }
    public static void open(UnaryOperator<Doll> task ,Doll doll){

        while ((doll= task.apply(doll))!=null){
            System.out.print("X");
        }
    }

    public static void main(String[] args){

        open(s->{
            if (s.layer<=0) return null;
            else return  new Doll(--s.layer);
        },new Doll(5));
    }
}
