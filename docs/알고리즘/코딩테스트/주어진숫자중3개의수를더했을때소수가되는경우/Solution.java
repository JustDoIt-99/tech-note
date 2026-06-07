import java.util.*;

class Solution {

    private int[] selected = new int[3];
    private int answer = 0;

    public int solution(int[] nums) {
        dfs(nums, 0, 0);
        return answer;
    }

    private void dfs(int[] nums, int start, int depth) {
        if(depth == 3) {
            int sum = selected[0] + selected[1] + selected[2];
            if(isPrime(sum)) {
                answer++;
            }

            return;
        }

        for(int i = start; i < nums.length; i++) {
            selected[depth] = nums[i];
            dfs(nums, i + 1, depth + 1);
        }
    }

    private boolean isPrime(int num) {
        if(num < 2) return true;

        for(int i = 2; i * i <= num; i++) {
            if(num % i == 0) {
                return false;
            }
        }

        return true;
    }
}