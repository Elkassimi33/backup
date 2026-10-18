package med;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ListNode {

    public int val;
    public ListNode next;
    public ListNode(int val) {
        this.val = val;
    }
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }


//    public static ListNode mergeTwoSortedLists(ListNode list1, ListNode list2) {
//        ListNode head = new ListNode(0);
//        ListNode cur = head;
//        ListNode head1=list1;
//        ListNode head2 = list2;
//        while (head1!=null||head2!=null){
//
//            if (head1!=null && head2!=null){
//                if (head1.val>=head2.val){
//                    cur.next = new ListNode(head2.val);
//                    head2=head2.next;
//                    cur=cur.next;
//                }else {
//                    cur.next = new ListNode(head1.val);
//                    head1=head1.next;
//                    cur=cur.next;
//                }
//            }
//
//            else if (head1!=null) {
//                while (head1!=null){
//                    cur.next = new ListNode(head1.val);
//                    head1=head1.next;
//                    cur=cur.next;
//                }
//                break;
//            }
//
//            else {
//                while (head2!=null){
//                    cur.next = new ListNode(head2.val);
//                    head2=head2.next;
//                    cur=cur.next;
//                }
//                break;
//            }
//        }
//        return head.next;
//    }
//
//    public static ListNode mergeKSortedLists(ListNode[] lists){
//
//        if (lists.length == 1) return lists[0];
//        if(lists.length == 2) return  mergeTwoSortedLists(lists[0],lists[1]);
//
//        int mid = lists.length/2;
//
//        ListNode left = mergeKSortedLists(Arrays.copyOfRange(lists,mid,lists.length));
//        ListNode right = mergeKSortedLists(Arrays.copyOfRange(lists,0,mid));
//
//        return mergeTwoSortedLists(left,right);
//
//    }
//
    public static void display(ListNode l){

        ListNode cur =l;
        System.out.print('[');
        while (cur!=null) {
            System.out.print(" "+cur.val+' ');
            cur = cur.next;
        }
        System.out.print(']');
    }
//
//    private static void displayReverse(ListNode head,ListNode tail){
//        ListNode cur=head;
//        if (head.equals(tail)) {
//            return;
//        }
//        if(cur.equals(tail))
//            System.out.println();
//    }
//
//
//    private class  MergeTowList  extends RecursiveTask<ListNode>{
//
//
//
//        @Override
//        protected ListNode compute() {
//            return null;
//        }
//    }

    public static ListNode addTowNumber(ListNode l1, ListNode l2) {
        int carry = 0;
        ListNode result = new ListNode(0);
        ListNode cur = result;
        int sum ;
        while (l1 != null || l2 != null || carry != 0){
            cur.next = new ListNode(0);
            cur = cur.next;
            sum = carry;
            if (l1 != null){
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null){
                sum += l2.val;
                l2 = l2.next;
            }

           cur.val = sum % 10;
           carry = sum / 10;
        }
        return  result.next;
    }

    public ListNode removeNthFromEnd(ListNode head, int n){
        if (head == null) return null;
        int listLength = 0;
        ListNode cur = head;

        while (cur != null) {
            listLength ++;
            cur = cur.next;
        }
        if (listLength == n) return  head.next;

        cur = head;

        int counter = 0;
        while (counter < listLength - n -1) {
            cur = cur.next;
            counter++;
        }

        // if it is the last node
        if (cur.next.next == null){
            cur.next = null;
            return head;
        }


        ListNode temp = cur.next;
        cur.next = temp.next;
        temp.next = null;

        return  head;
    }

    public ListNode modifiedList(int[] nums, ListNode head) {
        if (head == null) return null;
        Set<Integer> numsToSet = Arrays.stream(nums).boxed().collect(Collectors.toSet());

        ListNode dummy = new ListNode(0,head);
        ListNode cur = dummy;
        while (cur != null){
            if (cur.next != null && numsToSet.contains(cur.next.val)){
                if (cur.next.next == null){
                    cur.next = null;
                    return dummy.next;
                }

                cur.next = cur.next.next;
                 continue;
            }
            cur = cur.next;
        }
        return  dummy.next;


    }




}

