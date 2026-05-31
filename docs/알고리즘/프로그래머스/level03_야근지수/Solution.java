import java.util.*;

class Solution {
    // 야근 피로도 = 야근 시작 시점에서 남은 일의 작업량의 제곱후 더한 값
    // 균등하게 만드는 것이 제일 중요 -> 제일 큰 수를 줄이는 방향으로
    // 2^10 = 1,000
    // 2^20 = 1,000,000
    public long solution(int n, int[] works) {
        long answer = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for(int i = 0; i < works.length; i++) { // O(20,000)
            pq.offer(works[i]); // O(log20,000) = 14
        }

        for(int i = 1; i <= n; i++) { // O(1,000,000)
            pq.offer(Math.max(0,pq.poll() - 1));
        }

        while(pq.size() != 0) { // O(20,000)
            long value = pq.poll();
            answer += value * value;
        }

        return answer;
    }
}