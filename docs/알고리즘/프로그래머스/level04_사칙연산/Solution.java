import java.util.*;

class Solution {

    private int[][] minDp;
    private int[][] maxDp;

    public int solution(String arr[]) {
        int answer = -1;

        int numCnt = arr.length / 2 + 1; // 숫자 갯수

        // 숫자로만
        minDp = new int[numCnt][numCnt];
        maxDp = new int[numCnt][numCnt];

        for(int i = 0; i < numCnt; i++) {
            Arrays.fill(minDp[i], Integer.MAX_VALUE);
            Arrays.fill(maxDp[i], Integer.MIN_VALUE);
        }

        // 0,2,4,6
        // 1,3,5,7
        for(int i = 0; i < numCnt; i++) {
            minDp[i][i] = Integer.parseInt(arr[i * 2]);
            maxDp[i][i] = Integer.parseInt(arr[i * 2]);
        }

        //[1,3,5,8]

        //0,1,2,3
        //0,1,2,3,4,5,6

        // 최댓값
        // 뺄셈? 최대 - 최소
        // 덧셈? 최대 + 최대
        // 결론적으로 최대랑 최소 둘다 구해줘야 한다.
        // 구간DP.. 반드시 정복한다.

        for(int len = 2; len <= numCnt; len++) { // 구간
            for(int start = 0; start <= numCnt - len; start++) { // 시작점
                int end = start + len - 1; // 마지막 지점 index
                // 어디서 중간으로 나눌것인가?
                for(int mid = start; mid < end; mid++) { // mid -> 왼쪽 구간의 끝
                    String op = arr[mid * 2 + 1];

                    if(op.equals("+")) {
                        minDp[start][end] = Math.min(minDp[start][end], minDp[start][mid] + minDp[mid + 1][end]);
                        maxDp[start][end] = Math.max(maxDp[start][end], maxDp[start][mid] + maxDp[mid + 1][end]);
                    }else { // 뺄셈의 최소 : 작은 수 - 큰수, 최대 : 큰수 - 작은수
                        minDp[start][end] = Math.min(minDp[start][end], minDp[start][mid] - maxDp[mid + 1][end]);
                        maxDp[start][end] = Math.max(maxDp[start][end], maxDp[start][mid] - minDp[mid + 1][end]);
                    }
                }
            }
        }

        return maxDp[0][numCnt -1];
    }
}