import med.LeetCode;
import med.ListNode;

import java.math.BigDecimal;
import java.util.*;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LeetCode solution = new LeetCode();
//        ListNode test = new ListNode(0);
//        //[1,2,3,4,5]
//        ListNode list1 = new ListNode(1);
//        ListNode cur = list1;
//        for (int i = 2; i <6 ; i++) {
//            cur.next = new ListNode(i);
//            cur = cur.next;
//        }
//        cur.next = null;
//
//        ListNode.display(list1);
//        System.out.println();
//        ListNode.display(test.modifiedList(new int[]{1,2,5}, list1));
////        ListNode.display(test.removeNthFromEnd(list1, 2));


//        int d = 4;
//        List<Integer> arr = List.of(1,2,3,4,5);
//        List<Integer> sol = new ArrayList<>(arr.size());
//        for(int i = 0 ; i < arr.size() ; i++){
//            sol.add(0);
//        }
//        for(int i = 0 ; i < arr.size() ; i++){
//            int position = (i-d) < 0 ? (arr.size() + (i -d) ): i-d;
//            sol.set(position, arr.get(i));
//        }

//        solution.firstMissingPositive(new int[]{3,2,-1,1});
//        System.out.println(solution.firstMissingPositive(new int[]{3,2,-1,1}));


        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        String []s=new String[n+2];
        for(int i=0;i<n;i++){
            s[i]=sc.next();
        }
        sc.close();

        Map<BigDecimal,List<String>> m = new HashMap<>();
        BigDecimal[] r = new BigDecimal[n];

        for(int i = 0; i < n; i++ ){
            r[i] = new BigDecimal(s[i]);
            m.computeIfAbsent(r[i], v->(new ArrayList<>()));
            m.get(r[i]).add(s[i]);

        }
        Arrays.sort(r, Comparator.reverseOrder());

        for(int i=0;i<n;i++)
        {
            if(m.containsKey(r[i])){
                for(String num : m.get(r[i])){
                    System.out.println(num);
                    m.remove(r[i]);
                }
            }
        }

        PriorityQueue
    }
}

class Checker implements Comparator<Player>{

    public int compare(Player p1, Player p2){
        if(p1.score != p2.score)
            return Integer.compare(p1.score, p2.score);
        else
            return p1.name.compareTo(p2.name);
    }
}

class Player{
    public String name;
    public int score;

    public Player(String name, int score){
        this.name = name;
        this.score = score;
    }

}