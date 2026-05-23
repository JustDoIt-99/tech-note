import java.util.*;

class Solution {

    private static Set<Integer> rootCheck = new HashSet<>();
    private static int n;
    private int groupCount = 0;
    private int k;
    private int[] num;
    private int[][] links;

    public int solution(int k, int[] num, int[][] links) {
        int answer = 0;

        this.num = num;
        this.k = k;
        this.links = links;

        int left = Arrays.stream(num).max().getAsInt();
        int right = Arrays.stream(num).sum();
        n = num.length;

        for(int i = 0; i < n; i++) {
            rootCheck.add(i);
        }

        int root = findRoot();
        return search(left, right, root);
    }

    private int search(int left, int right, int root) { // 인원이 가장 많은 그룹의 인원이 최소화
        int mid = 0;
        int answer = 0;
        while(left <= right) {
            mid = (left + right) / 2;

            if(check(mid, root))  {
                answer = mid;
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }

        return answer;
    }

    private boolean check(int limit, int root) {
        groupCount = 1; // 초기화
        if(checkDfs(root, limit) == -1) { // -1을 반환하는 경우
            return false;
        }

        return groupCount <= k;
    }

    // 현재 노드 + 왼쪽 서브트리 합 + 오른쪽 서브 트리합
    private int checkDfs(int cur, int limit) {

        if(num[cur] > limit) return -1;

        int left = 0;
        int right = 0;
        int leftChild = links[cur][0];
        int rightChild = links[cur][1];

        if(leftChild != -1) { // 자식이 있는 경우
            left += checkDfs(leftChild, limit);
            if(left == -1) return -1;
        }

        if(rightChild != -1) {
            right += checkDfs(rightChild, limit);
            if(right == -1) return -1;
        }

        int total = left + right + num[cur]; // 서브 트리의 합

        if(total <= limit) {
            return total;
        }

        if(num[cur] + Math.min(left, right) <= limit) { // 더 작은 수를 더하기
            groupCount++; // 그룹 나누기
            return num[cur] + Math.min(left, right);
        }

        groupCount += 2; // 그룹 둘로 나누기
        return num[cur];
    }

    private int findRoot() {

        for(int i = 0; i < n; i++) {
            if(links[i][0] != -1) rootCheck.remove(links[i][0]);
            if(links[i][1] != -1) rootCheck.remove(links[i][1]);
        }

        return rootCheck.iterator().next();
    }
}