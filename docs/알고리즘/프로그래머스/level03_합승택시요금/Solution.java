import java.util.*;

class Solution {

    private List<List<int[]>> graph = new ArrayList<>();
    private int N;

    public int solution(int n, int s, int a, int b, int[][] fares) {
        N = n;

        for(int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for(int[] fare: fares) {
            int from = fare[0];
            int to = fare[1];
            int cost = fare[2];
            graph.get(from).add(new int[]{to, cost});
            graph.get(to).add(new int[]{from, cost});
        }

        int answer = Integer.MAX_VALUE;

        // 중간 지점을 어디로 할 것인가?
        for(int i = 1; i <= n; i++) {
            int[] dist = findShortestPath(i);
            answer = Math.min(answer, dist[s] + dist[a] + dist[b]);
        }

        return answer;
    }

    private int[] findShortestPath(int start) {
        int[] dist = new int[N + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        PriorityQueue<int[]> pq = new PriorityQueue<>((int[] a, int[] b) -> {
            return a[1] - b[1]; // 거리별 오름차순
        });
        pq.offer(new int[]{start, 0});
        dist[start] = 0;

        while(pq.size() != 0) {
            int[] cur = pq.poll();
            int curNumber = cur[0];
            int curDist = cur[1];

            if(curDist > dist[curNumber]) continue;

            for(int[] next: graph.get(curNumber)) {
                int nNumber = next[0];
                int nValue = next[1];

                if(dist[nNumber] > curDist + nValue) {
                    dist[nNumber] = curDist + nValue;
                    pq.offer(new int[]{nNumber, curDist + nValue});
                }
            }
        }

        return dist;
    }
}