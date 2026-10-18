 public  class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int x) { val = x; }
    ListNode(int x, ListNode next) { this.val = x; this.next = next; }

    public void print() {
        ListNode cur = this;
        while(cur!=null) {
            System.out.print(cur.val + " ");
            cur = cur.next;

        }
    }
}