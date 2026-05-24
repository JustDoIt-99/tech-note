import java.util.*;

class Solution {

    public static class Node {
        Node left;
        Node right;
        int cur;
        int x;

        public Node(int cur, int x) {
            this.cur = cur;
            this.x = x;
        }
    }

    private HashMap<Integer, Integer> xNum = new HashMap<>();
    private int[] preRes;
    private int preIdx = 0;
    private int[] postRes;
    private int postIdx = 0;

    // 노드 간의 관계를 알아야 한다.
    public int[][] solution(int[][] nodeinfo) {
        int[][] answer = {};

        int n = nodeinfo.length;

        if(n == 1) return new int[][]{{1},{1}};

        for(int i = 0; i < n; i++) { // O(n)
            int x = nodeinfo[i][0];
            int y = nodeinfo[i][1];

            xNum.put(x, i + 1); // (x좌표, 노드 번호) 매칭
        }

        Arrays.sort(nodeinfo, (int[] a, int[] b) -> { // O(nlogn)
            if(a[1] == b[1]) return a[0] - b[0];
            return b[1] - a[1];
        });

        Node root = new Node(xNum.get(nodeinfo[0][0]), nodeinfo[0][0]);

        preRes = new int[n];
        postRes = new int[n];

        for(int i = 1; i < n; i++) { // O(logN)
            insert(root, nodeinfo[i][0]);
        }

        preOrder(root); // O(E + V)
        postOrder(root); // O(E + V)

        postRes[n - 1] = root.cur;
        return new int[][]{preRes, postRes};
    }

    private void preOrder(Node cur) {
        preRes[preIdx++] = cur.cur;
        if(cur.left!=null) preOrder(cur.left);
        if(cur.right!=null) preOrder(cur.right);
    }

    private void postOrder(Node cur) {
        if(cur.left != null) postOrder(cur.left);
        if(cur.right != null) postOrder(cur.right);
        postRes[postIdx++] = cur.cur;
    }

    private void insert(Node cur, int target) {
        int curx = cur.x;
        if(target < curx) {
            if(cur.left == null) {
                cur.left = new Node(xNum.get(target), target);
                return;
            }

            insert(cur.left, target);
        }else {
            if(cur.right == null) {
                cur.right = new Node(xNum.get(target), target);
                return;
            }

            insert(cur.right, target);
        }
    }

}