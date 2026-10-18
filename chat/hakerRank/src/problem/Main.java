package problem;

import java.util.ArrayList;
import java.util.List;
public class Main {

    public static int threadNumber(int[] startTab, int[] endTab){
        List<Integer> thread = new ArrayList<>();
        thread.add(endTab[0]);
        for(int i=1;i<startTab.length;i++){
            int starPoint=startTab[i];
            boolean isAdded=false;
            for (int j=0;j<thread.size();j++){
                if(starPoint>=thread.get(j)) {
                    thread.set(j, endTab[i]);
                    isAdded=true;
                }
            }
            if(!isAdded){
                thread.add(endTab[i]);
            }

        }
        return thread.size();
    }
    public static void main(String[] args){
        int []t1={2,2,2,2,2};
        int t2[]={3,3,3,4,7};
        System.out.println(threadNumber(t1,t2));
    }
}