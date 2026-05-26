import java.util.*;

class Solution {

    public int solution(int[][] triangle) {
        int n = triangle.length;

        for(int i = 0; i < triangle.length; i++) {
            int m = triangle[i].length;
            if(i == 0) continue;
            for(int j = 0; j < m; j++) {
                if(j == 0) {
                    triangle[i][j] += triangle[i-1][j];
                }else if(j == m - 1) {
                    triangle[i][j] += triangle[i-1][j-1];
                }else {
                    triangle[i][j] += Math.max(triangle[i-1][j-1], triangle[i-1][j]);
                }

            }
        }

        int answer = Integer.MIN_VALUE;
        for(int i = 0; i < triangle[n - 1].length; i++) {
            answer = Math.max(answer, triangle[n - 1][i]);
        }

        return answer;
    }
}