import java.util.*;

class Solution {
    // 1. 루트 노드 찾기
    // 2. 이분 탐색

    private int[][] links;
    private int[] num;
    private Set<Integer> checkRoot = new HashSet<>();
    private int k;
    private int n;
    private int groupCount;

    public int solution(int k, int[] num, int[][] links) {
        int answer = 0;

        this.k = k;
        this.links = links;
        this.num = num;
        this.n = num.length;

        int root = findRoot();

        return binarySearch(root);
    }

    private int binarySearch(int root) {

        int left = Arrays.stream(num).max().getAsInt();
        int right = Arrays.stream(num).sum();
        int answer = 0;

        while(left <= right) {
            int mid = (left + right) / 2;

            if(check(mid, root)) {
                answer = mid;
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }

        return answer;
    }

    private boolean check(int limit, int root) {
        groupCount = 1;
        if(dfs(root, limit) == -1) return false;
        return groupCount <= k;
    }

    private int dfs(int cur, int limit) {
        int total = 0;
        int left = 0;
        int right = 0;

        if(num[cur] > limit) return -1;

        int leftChild = links[cur][0];
        int rightChild = links[cur][1];

        if(leftChild != -1) { // 왼쪽 서브 트리
            left += dfs(leftChild, limit);
            if(left == -1) return -1;
        }

        if(rightChild != -1) { // 오른쪽 서브 트리
            right += dfs(rightChild, limit);
            if(right == -1) return -1;
        }

        total = left + right + num[cur];

        if(total <= limit) {
            return total;
        }

        if(num[cur] + Math.min(left, right) <= limit) { // 자식중 큰거 잘라
            groupCount++;
            return num[cur] + Math.min(left, right);
        }

        groupCount += 2; // 둘다 잘라
        return num[cur];
    }

    private int findRoot() {

        for(int i = 0; i < n; i++) {
            checkRoot.add(i);
        }

        for(int i = 0; i < n; i++) {
            int left = links[i][0];
            int right = links[i][1];
            checkRoot.remove(left);
            checkRoot.remove(right);
        }

        return checkRoot.iterator().next();
    }
}