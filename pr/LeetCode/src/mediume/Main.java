package mediume;

import java.util.Scanner;

/**
 * @author elkay
 **/
public class Main {
    public int divide(int dividend, int divisor) {
        boolean signe = (dividend<0&&divisor<0)||(dividend>0&&divisor>0);
        if(divisor==-2147483648){
            if(dividend==-2147483648)
                return 1;
            else
                return 0;
        }
        boolean change=false;
        if(dividend==-2147483648){
            change =true;
            dividend+=1;
        }
        divisor=Math.abs(divisor);
        dividend=Math.abs(dividend);
        int i=0;
        while(dividend>=divisor){
            dividend-=divisor;
            if(change){
                dividend+=1;
                change =false;
            }
            i++;
        }
        return signe ? i:-i;

    }
    private static int  inverseNumber(int x) throws NumberFormatException {
            boolean signe = x<0;
            x=Math.abs(x);
            StringBuilder numString =new StringBuilder();
            if(x==0)
                return 0;

            while(x!=0){
                numString.append(x%10);
                x=x/10;
            }
            int result;
            try{
                result =Integer.parseInt(numString.toString());
            }catch(NumberFormatException e){
                throw new NumberFormatException();

            }
            return  signe ? -result:result;
        }
    public static void main(String[] args)  {
//     Scanner c =new Scanner(System.in);
//        int x;
//        while(( x= c.nextInt())!=-0) {
//            System.out.println(inverseNumber(x));
//        }
        System.out.println(new Main().divide(-2147483648,-1));

       // int a = Integer.parseInt("1234544744444554");

    }
}
