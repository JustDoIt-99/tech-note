import java.util.*;

class Solution {
    // 합이 S가 되는 수의 집합이면서 원소의 곱이 최대가 되는 집합
    // 두 수의 차이가 제일 작은 집합이 원소의 곱이 최대가 되지 않나?
    public int[] solution(int n, int s) {

        if(n > s) return new int[]{-1};

        int v = s / n;
        int t = s % n;

        int[] answer = new int[n];
        int idx = 0;

        Arrays.fill(answer, v);

        if(t == 0) {
            return answer;
        }

        for(int i = n - t; i < n; i++) {
            answer[i] = v + 1;
        }

        return answer;
    }
}