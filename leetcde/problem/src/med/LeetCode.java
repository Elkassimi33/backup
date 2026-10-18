package med;

import java.math.BigInteger;
import java.util.*;

public class LeetCode {

    public static double  median(int[] nums1, int[] nums2){

        int[] merged = new int[nums1.length + nums2.length];
        System.arraycopy(nums1, 0, merged, 0, nums1.length);
        System.arraycopy(nums2, 0, merged, nums1.length,nums2.length);
        Arrays.sort(merged);
        int length = merged.length;
        if (length == 1) return merged[0];
        if(length % 2 == 1) return merged[length / 2];
        else return (merged[length /2 - 1] +merged[length / 2]) / 2.0;

    }

    public static  int reverseInteger(int x){
        boolean signe = x < 0;
        long l = Math.abs((long) x);
        String reversed = String.valueOf(new StringBuilder(Long.valueOf(l).toString()).reverse());
        long result =  signe ? -Long.parseLong(reversed) : Long.parseLong(reversed) ;

        if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) return  0;
        else return (int) result;
    }
    public static int reverseIntegerV2(int x){
        boolean signe = x < 0;
        long l = Math.abs((long) x);
        long result = 0;
        while (l != 0){
            result *= 10 ;
            result += l%10;
            l = l / 10;
        }
        result =  signe ? -result : result;
        if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) return  0;
        else return (int) result;
    }

    public static double medianOfTowSortedArray(int[] nums1, int[] nums2){
        int[] merged = new int[nums1.length + nums2.length];
        int i = 0, j = 0, k = 0;
        while (i < nums1.length || j < nums2.length){
            if (i < nums1.length && j < nums2.length){
                    merged[k++] =  nums1[i] < nums2[j] ? nums1[i++] : nums2[j++];
            } else if (i < nums1.length) {
                merged[k++] = nums1[i++];
            }else{
                merged[k++] = nums2[j++];
            }
        }
        if (merged.length == 1) return merged[0];
        if(merged.length % 2 == 1) return merged[merged.length / 2];
        else return (merged[merged.length /2 - 1] +merged[merged.length / 2]) / 2.0;

    }

    public  int lengthOfLongestSubstring(String s) {
        if (s.isEmpty()) return 0;
        int maxLength = 0, currentLength = 0;
        int startOfSubString = 0;
        Map<Character,Integer> store = new HashMap<>();
        int k = 0;
        while (k < s.length()){
            if (!store.containsKey(s.charAt(k)) || store.get(s.charAt(k)) < startOfSubString ){
                store.put(s.charAt(k), k);
                currentLength ++;
                k++;
            }else{
                maxLength = Math.max(maxLength, currentLength);
                currentLength = k - store.get(s.charAt(k));
                startOfSubString = store.get(s.charAt(k)) + 1;
                store.put(s.charAt(k), k);
                k++;
            }
        }
        return  Math.max(maxLength, currentLength);
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Set<ListNode> headAToSet = new HashSet<>();
        ListNode curA = headA, curB = headB;
        while (curA != null){
            headAToSet.add(curA);
            curA = curA.next;
        }
        while (curB != null){
            if (headAToSet.contains(curB)) return  curB;
            curB = curB.next;
        }
        return  null;
    }

    public Node copyRandomList(Node head) {
        if (head == null) return  null;
        List<Node> nodeList = new ArrayList<>();
        List<Node> oldList = new ArrayList<>();
        oldList.add(head);
        int k = 0;
        nodeList.add(new Node(head.val));
        Node cur = head.next;
        while (cur != null){
            oldList.add(cur);
            Node node = new Node(cur.val);
            nodeList.get(k).next = node;
            nodeList.add(node);
            k++;
            cur = cur.next;
        }
        cur = head;
        Node cur2 = nodeList.getFirst();
        while (cur2 != null){
           cur2.random = cur.random == null ? null : nodeList.get(oldList.indexOf(cur2.random));
            cur2 = cur2.next;
            cur = cur.next;
        }
        return nodeList.getFirst();
    }

    public String longestCommonPrefix(String[] strs) {
        int minLength = Arrays.stream(strs).mapToInt(String::length).min().orElse(0);
        if(minLength == 0) return "";
        StringBuilder result = new StringBuilder();
        outerLoop:
        for(int i = 0; i < minLength; i++){
            char c =  strs[0].charAt(i);
            for (String str : strs) {
                if (c != str.charAt(i)) {
                    break outerLoop;
                }
            }
            result.append(c);
        }
        return result.toString();
    }
    public int myAtoi(String s) {
        if(s == null || s.trim().isEmpty()) return 0;
        s = s.trim();
        boolean isNegative = false ;
        if(s.charAt(0) == '-' || s.charAt(0) == '+'){
            isNegative = s.charAt(0) == '-';
            s = s.substring(1);
        }
        long result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                if(result * 10 < result) break;
                result *= 10;
                if((result + (s.charAt(i) - '0')) < result) break;
                result += (s.charAt(i) - '0');
            } else break;
        }

        result = isNegative ? -result : result;
        if (!isNegative && Integer.MAX_VALUE < result) return Integer.MAX_VALUE;
        if (isNegative && Integer.MIN_VALUE > result) return Integer.MIN_VALUE;
        return (int)result;
    }

    public  int queensAttack(int n, int k, int r_q, int c_q, List<List<Integer>> obstacles) {
        if (n == 0) return 0;

        // Instead of a full n x n grid, store only obstacle positions.
        // Encode (r, c) as a single long: r * (n + 1) + c
        Set<Long> obstacleSet = new HashSet<>();
        for (List<Integer> obstacle : obstacles) {
            int r = obstacle.get(0);
            int c = obstacle.get(1);
            obstacleSet.add((long) r * (n + 1) + c);
        }

        int result = 0;

        // Up (increasing row)
        for (int i = r_q + 1; i <= n; i++) {
            if (obstacleSet.contains((long) i * (n + 1) + c_q)) break;
            result++;
        }

        // Down (decreasing row)
        for (int i = r_q - 1; i >= 1; i--) {
            if (obstacleSet.contains((long) i * (n + 1) + c_q)) break;
            result++;
        }

        // Right (increasing column)
        for (int j = c_q + 1; j <= n; j++) {
            if (obstacleSet.contains((long) r_q * (n + 1) + j)) break;
            result++;
        }

        // Left (decreasing column)
        for (int j = c_q - 1; j >= 1; j--) {
            if (obstacleSet.contains((long) r_q * (n + 1) + j)) break;
            result++;
        }

        // Up-Right (increasing row, increasing column)
        for (int i = r_q + 1, j = c_q + 1; i <= n && j <= n; i++, j++) {
            if (obstacleSet.contains((long) i * (n + 1) + j)) break;
            result++;
        }

        // Down-Left (decreasing row, decreasing column)
        for (int i = r_q - 1, j = c_q - 1; i >= 1 && j >= 1; i--, j--) {
            if (obstacleSet.contains((long) i * (n + 1) + j)) break;
            result++;
        }

        // Up-Left (increasing row, decreasing column)
        for (int i = r_q + 1, j = c_q - 1; i <= n && j >= 1; i++, j--) {
            if (obstacleSet.contains((long) i * (n + 1) + j)) break;
            result++;
        }

        // Down-Right (decreasing row, increasing column)
        for (int i = r_q - 1, j = c_q + 1; i >= 1 && j <= n; i--, j++) {
            if (obstacleSet.contains((long) i * (n + 1) + j)) break;
            result++;
        }

        return result;
    }

    public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {

        int r_k = king[0];
        int c_k = king[1];
        int n = 8;
        Set<Long> obstacleSet = new HashSet<>();
        for (int[] queen : queens) {
            int r = queen[0];
            int c = queen[0];
            obstacleSet.add((long) r * n + c);
        }

        List<List<Integer>> result = new ArrayList<>();


        for (int i = r_k ; i < n; i++) {
            if (obstacleSet.contains((long) i * n + c_k)){
                result.add(List.of(i,c_k));
                break;
            }
        }

        // Down (decreasing row)
        for (int i = r_k ; i >= 0; i--) {
            if (obstacleSet.contains((long) i * n  + c_k)){
                result.add(List.of(i, c_k));
                break;
            }
        }

        // Right (increasing column)
        for (int j = c_k ; j <=n; j++) {
            if (obstacleSet.contains((long) r_k * n  + j)){
                result.add(List.of(r_k, j));
                break;
            }
        }

        // Left (decreasing column)
        for (int j = c_k ; j >= 0; j--) {
            if (obstacleSet.contains((long) r_k * n  + j)) {
                result.add(List.of(r_k, j));
                break;
            }
        }

        // Up-Right (increasing row, increasing column)
        for (int i = r_k , j = c_k ; i < n && j < n; i++, j++) {
            if (obstacleSet.contains((long) i * n + j)) {
                result.add(List.of(i, j));
                break;
            }
        }

        // Down-Left (decreasing row, decreasing column)
        for (int i = r_k , j = c_k ; i >= 0 && j >= 0; i--, j--) {
            if (obstacleSet.contains((long) i * n  + j)) {
                result.add(List.of(i, j));
                break;
            }
        }

        // Up-Left (increasing row, decreasing column)
        for (int i = r_k , j = c_k ; i < n && j >= 0; i++, j--) {
            if (obstacleSet.contains((long) i * n  + j)) {
                result.add(List.of(i, j));
                break;
            }
        }

        // Down-Right (decreasing row, increasing column)
        for (int i = r_k - 1, j = c_k + 1; i >= 0 && j < n; i--, j++) {
            if (obstacleSet.contains((long) i * n + j)){
                result.add(List.of());
                break;
            }
        }

        return result;
    }

    /**
     * @param nums
     * @return
     */
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // Place each number in its correct position
        for (int i = 0; i < n; i++) {
            // While nums[i] is in the range [1, n] and not in its correct position
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                // Swap nums[i] with nums[nums[i] - 1]
                int temp = nums[i];
                nums[i] = nums[temp - 1];
                nums[temp - 1] = temp;
            }
        }

        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n + 1;
    }

    public void validTags(String[] lines) {
        for(String line: lines) {
            Stack<String> tage = new Stack<>();
            if (!line.startsWith("<")) {
                System.out.println("None");
                continue;
            }
            tage.push(line.substring(1, line.indexOf(">")));

            StringBuilder lineBuilder = new StringBuilder();
            int i;
            int lineStart = 0;
            for (i = line.indexOf(">"); i < line.length(); i++) {
                if (line.charAt(i) != '<') {
                    lineBuilder.append(line.charAt(i));
                } else {
                    if (line.charAt(i + 1) != '/' ) {
                        if(!lineBuilder.isEmpty() && !lineBuilder.substring(lineStart).isEmpty())
                            lineBuilder = new StringBuilder(lineBuilder.substring(lineStart));
                    }
                }
            }

        }
    }

    public  int maxUnique(int[] nums, int m){
        Deque<Integer> deque = new ArrayDeque<>();
        Map<Integer, Integer> frequency = new HashMap<>();
        int maxUnique = 0;

        for (int num : nums) {
            // Add current element
            deque.add(num);
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);

            // If window size exceeds m, remove from front
            if (deque.size() > m) {
                int removed = deque.removeFirst();
                frequency.put(removed, frequency.get(removed) - 1);
                if (frequency.get(removed) == 0) {
                    frequency.remove(removed);
                }
            }

            // Update max when window reaches size m
            if (deque.size() == m) {
                maxUnique = Math.max(maxUnique, frequency.size());
            }
        }

        return  maxUnique;

    }
}
