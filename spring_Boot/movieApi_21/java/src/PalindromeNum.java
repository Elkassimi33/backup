public class PalindromeNum {

    public static boolean isPalindrome(int x) {

        if(x<0)
            return false;
        else if(x<10)
            return true;
        else{
            String A=Integer.toString(x);
            int j=A.length()-1;
            int i=0;
            while(i<j){
                if(A.charAt(i)!=A.charAt(j))
                    break;
                j--;
                i++;
            }

            return i>=j;
        }
//        if(x<0)
//            return false;
//        else if(x<10)
//            return true;
//        else{
//            String str=Integer.toString(x);
//            String sub1;
//            String sub2;
//            StringBuilder sb = null;
//            if(str.length()%2==0){
//                sub1=str.substring(str.length()/2);
//                sub2=str.substring(0,str.length()/2);
//                sb=new StringBuilder(sub1);
//            }
//            else{
//                sub1=str.substring(str.length()/2+1);
//                sub2=str.substring(0,str.length()/2);
//                 sb=new StringBuilder(sub1);
//            }
//            return sub2.equals(sb.reverse().toString());
//        }

    }

    public static void main(String[] args) {
        System.out.println(PalindromeNum.isPalindrome(121));

    }
}

class Solution {
    public boolean isPalindrome(int x) {
        if(x<0)
            return false;
        else if(x<10)
            return true;
        else{
            String str=Integer.toString(x);
            String sub1;
            String sub2;
            StringBuilder sb = null;
            if(str.length()%2==0){
                sub1=str.substring(str.length()/2);
                sub2=str.substring(0,str.length()/2);
                sb=new StringBuilder(sub1);
            }
            else{
                sub1=str.substring(str.length()/2+1);
                sub2=str.substring(0,str.length()/2);
                sb=new StringBuilder(sub1);
            }
            return sub2.equals(sb.reverse().toString());

        }
    }
}