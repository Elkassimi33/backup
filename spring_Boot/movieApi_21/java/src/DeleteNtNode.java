public class DeleteNtNode {

    public static ListNode deleteNthFromEnd(ListNode head, int n) {
        int listSize = 0;
        ListNode listNode = head;
        while (listNode!= null) {
            listSize++;
            listNode = listNode.next;
        }
        if (listSize == n) {
            assert head != null;
            return head.next;
        }
        listNode=head;
        for (int i = 0; i < listSize-n-1; i++) {
            listNode = listNode.next;
        }
        try {
            listNode.next=listNode.next.next;
        }
        catch (NullPointerException e) {
            listNode.next=null;
        }

        return head;
    }

    public static void main(String[] args) {

        ListNode head2 = new ListNode(9);
        ListNode temp2 = head2;


        temp2.next=new ListNode(3);
        temp2=temp2.next;
        temp2.next=new ListNode(2);
        temp2=temp2.next;
        temp2.next=new ListNode(4);
        temp2=temp2.next;
        deleteNthFromEnd(head2,5);

    }
}
