import java.util.*;

class Solution {

    private boolean[] visited;
    private int[] dis;
    private List<List<Integer>> graph = new ArrayList<>();
    private int max = Integer.MIN_VALUE;

    public int solution(int n, int[][] edge) {
        visited = new boolean[n + 1]; // 방문 확인
        dis = new int[n + 1]; // n번 노드까지의 거리

        for(int i = 0; i <= n; i++) { // O(n) = 20,000
            graph.add(new ArrayList<>());
        }

        for(int i = 0; i < edge.length; i++) { // O(n) = 50,000
            int a = edge[i][0];
            int b = edge[i][1];

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        bfs();

        int answer = 0;
        for(int i = 0; i < dis.length; i++) { // O(n) = 20,000
            if(max == dis[i]) {
                answer++;
            }
        }

        return answer;
    }

    private void bfs() {
        Queue<Integer> queue = new LinkedList<>();
        visited[1] = true;
        queue.offer(1);

        while(!queue.isEmpty()) {
            int cur = queue.poll();
            for(int next : graph.get(cur)) {
                if(!visited[next]) {
                    visited[next] = true;
                    dis[next] = dis[cur] + 1;
                    max = Math.max(max, dis[next]);
                    queue.offer(next);
                }
            }
        }
    }
}