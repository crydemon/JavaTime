import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class TreeNode {

  int val;
  TreeNode left;
  TreeNode right;

  TreeNode() {
  }

  TreeNode(int val) {
    this.val = val;
  }

  TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }
}

class ListNode {

  int val;
  ListNode next;

  ListNode(int x) {
    val = x;
  }
}

public class Solution {


  TreeNode node = null;

  public boolean lowestCommonAncestorDFS(TreeNode node, TreeNode p, TreeNode q) {
    if (node == null) {
      return false;
    }
    boolean lson = lowestCommonAncestorDFS(node.left, p, q);
    boolean rson = lowestCommonAncestorDFS(node.right, p, q);

    if (lson && rson || (p == node || q == node) && (lson || rson)) {
      this.node = node;
    }
    return p == node || q == node || lson || rson;
  }

  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    lowestCommonAncestorDFS(root, p, q);
    return this.node;
  }

  public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    Map<String, Integer> map = new HashMap<>();
    List<TreeNode> ans = new ArrayList<>();
    findDuplicateSubtreesDFS(root, map, ans);
    return ans;
  }

  public String findDuplicateSubtreesDFS(TreeNode root, Map<String, Integer> map,
      List<TreeNode> ans) {
    if (root == null) {
      return "#";

    }
    String key = root.val + "," + findDuplicateSubtreesDFS(root.left, map, ans) + ","
        + findDuplicateSubtreesDFS(root.right, map, ans);
    int count = map.getOrDefault(key, 0) + 1;
    map.put(key, count);
    if (count == 2) {
      ans.add(root);
    }
    return key;
  }

  public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode head = dummy;
    while (l1 != null && l2 != null) {
      if (l1.val < l2.val) {
        head.next = l1;
        head = head.next;
        l1 = l1.next;
      } else {
        head.next = l2;
        head = head.next;
        l2 = l2.next;
      }
    }
    while (l1 != null) {
      head.next = l1;
      head = head.next;
      l1 = l1.next;
    }
    while (l2 != null) {
      head.next = l2;
      head = head.next;
      l2 = l2.next;
    }
    return dummy.next;
  }

  public TreeNode buildTree(int[] preorder, int[] inorder, int index, int il, int ir) {
    if (il > ir) {
      return null;
    }
    int val = preorder[index];
    int i = il;
    while (i <= ir && val != inorder[i]) {
      i++;
    }
    TreeNode node = new TreeNode(val);
    node.left = buildTree(preorder, inorder, index + 1, il, i - 1);
    int leftLen = i - il + 1;
    node.right = buildTree(preorder, inorder, index + leftLen, i + 1, ir);
    return node;
  }

  public TreeNode buildTree(int[] preorder, int[] inorder) {
    return buildTree(preorder, inorder, 0, 0, inorder.length - 1);
  }

  public TreeNode constructMaximumBinaryTree(int[] nums, int left, int right) {
    if (left > right) {
      return null;
    }
    int maxIndex = left;
    int i = left;
    while (++i <= right) {
      if (nums[i] > nums[maxIndex]) {
        maxIndex = i;
      }
    }
    TreeNode node = new TreeNode(nums[maxIndex]);
    node.left = constructMaximumBinaryTree(nums, left, maxIndex - 1);
    node.right = constructMaximumBinaryTree(nums, maxIndex + 1, right);
    return node;
  }

  public TreeNode constructMaximumBinaryTree(int[] nums) {
    return constructMaximumBinaryTree(nums, 0, nums.length - 1);
  }


  public String removeDuplicateLetters(String s) {
    int[] hash = new int[26];
    for (char c : s.toCharArray()) {
      hash[c - 'a']++;
    }
    Stack<Character> stack = new Stack<>();
    HashSet<Character> seen = new HashSet<>();
    for (char c : s.toCharArray()) {
      if (seen.contains(c)) {
        hash[c - 'a']--;
        continue;
      }
      while (!stack.isEmpty() && stack.peek() > c && hash[stack.peek() - 'a'] > 1) {
        hash[stack.peek() - 'a']--;
        seen.remove(stack.peek());
        stack.pop();
      }
      seen.add(c);
      stack.push(c);
    }
    final StringBuilder result = new StringBuilder();
    stack.stream().forEach(x -> result.append(x));
    return result.toString();
  }

  public int longestMountain(int[] A) {
    int max = 0;
    int[] left = new int[A.length];

    int[] right = new int[A.length];

    for (int i = 1; i < A.length; i++) {
      if (A[i - 1] < A[i]) {
        left[i] = left[i - 1] + 1;
      } else {
        left[i] = 0;
      }
      int rIdx = A.length - i;
      if (A[rIdx] < A[rIdx - 1]) {
        right[rIdx - 1] = A[rIdx] + 1;
      } else {
        right[rIdx - 1] = 0;
      }
    }
    for (int i = 0; i < A.length; i++) {
      if (left[i] > 0 && right[i] > 0) {
        max = Math.max(left[i] + right[i] + 1, max);
      }
    }
    return max;
  }

  public static void main(String[] args) {
    CharBuffer charBuffer = CharBuffer.allocate(10);
    Solution solution = new Solution();
    //System.out.println(solution.removeDuplicateLetters("bcabc"));
    System.out.println(solution.removeDuplicateLetters("cbacdcbc"));
  }
}