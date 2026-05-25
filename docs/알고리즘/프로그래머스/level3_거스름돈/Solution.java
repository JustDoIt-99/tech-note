import java.util.*;

class Solution {

    private int[] dp;

    public int solution(int n, int[] money) {

        int[] dp = new int[n + 1]; // 현재까지 본 동전들로 i원을 만드는 경우의 수

        dp[0] = 1; //
        for(int coin: money) { // O(100)
            for(int price = coin; price <= n; price++) { // O(100,000)
                dp[price] += dp[price - coin];
            }
        }

        return dp[n];
    }
}