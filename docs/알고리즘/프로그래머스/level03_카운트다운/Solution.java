import java.util.*;

class Solution {

    public static class Node {
        int cnt;
        int singleBull;

        public Node(int cnt, int singleBull) {
            this.cnt = cnt;
            this.singleBull = singleBull;
        }
    }

    List<int[]> darts = new ArrayList<>(); // score, singleBoo

    public int[] solution(int target) {

        for(int i = 1; i <= 20; i++) { // single
            darts.add(new int[]{i, 1});
            darts.add(new int[]{i * 2, 0});
            darts.add(new int[]{i * 3, 0});
        }

        // bool
        darts.add(new int[]{50, 1});

        Node[] dp = new Node[target + 1];
        dp[0] = new Node(0, 0);

        for(int score = 1; score <= target; score++) { // 100,000
            for(int[] dart: darts) { // 61
                // dart
                int dartScore = dart[0];
                int dartSingleBull = dart[1];

                if(score - dartScore < 0) continue;
                if(dp[score - dartScore] == null) continue;

                Node prev = dp[score - dartScore];

                Node candi = new Node(
                        prev.cnt + 1,
                        prev.singleBull + dartSingleBull
                );

                if(dp[score] == null || compare(dp[score], candi)) {
                    dp[score] = candi;
                }
            }
        }

        return new int[]{dp[target].cnt, dp[target].singleBull};
    }

    private boolean compare(Node cur, Node candi) {

        if(cur.cnt > candi.cnt) return true;
        if(cur.cnt == candi.cnt) {
            return cur.singleBull < candi.singleBull;
        }

        return false;
    }
}