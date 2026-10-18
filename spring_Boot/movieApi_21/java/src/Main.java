import java.util.LinkedList;
import java.util.List;

public class Main {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        StringBuilder sumString = new StringBuilder();

        int rest=0;
        while (l1 != null || l2 != null|| rest!=0) {
            int sum = rest;
            if (l1 != null&&l2 != null) {
                sum+=l1.val;
                sum+=l2.val;
                l1=l1.next;
                l2=l2.next;

            }
            else if (l1 != null) {
                sum+=l1.val;
                l1 = l1.next;
            }
            else if (l2 != null) {
                sum+=l2.val;
                l2 = l2.next;
            }
            rest=sum/10;
            sumString.insert(0,sum%10);
        }

        for(int i = 0; i< sumString.length(); i++){
            if(sumString.charAt(i)!='0'){
                break;
            }
            sumString.deleteCharAt(i);
            i--;
        }
        ListNode head=null;
        ListNode cur;
        for(int i = 0; i< sumString.length()&& i<Math.min(sumString.length(),100); i++){
            cur=new ListNode(sumString.charAt(i)-'0',head);
            head=cur;
        }

        return head;
    }


    public static void main(String[] args) {

        Main main = new Main();

        ListNode head1 =new ListNode(9);
        ListNode head2 = new ListNode(9);
        ListNode temp1 = head1;
        ListNode temp2 = head2;


        temp2.next=new ListNode(9);
        temp2=temp2.next;
        temp2.next=new ListNode(9);


        head1.print();
        System.out.println();
        head2.print();
        System.out.println();
        main.addTwoNumbers(head1, head2).print();


    }
}