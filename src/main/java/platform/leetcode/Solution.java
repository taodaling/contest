package platform.leetcode;


import java.util.*;

public class Solution {
    public static void main(String[] arg){
        new Solution().minimumOneBitOperations(16);
    }

    public int minimumOneBitOperations(int n) {
        if (n == 0) {
            return 0;
        }
        long[] cost = new long[40];
        cost[0] = 1;
        for (int i = 1; i < 40; i++) {
            cost[i] = cost[i - 1] + 1 + cost[i - 1];
        }

        long inf = (long) 1e18;
        long[][] dp = new long[40][2];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j] = inf;
            }
        }
        dp[0][0] = 0;
        dp[0][1] = 1;
        for (int i = 0; i < 40 - 1; i++) {
            for (int j = 0; j < 2; j++) {
                int bit = (n >>> i) & 1;
                bit ^= j;
                if (bit == 1) {
                    dp[i + 1][1] = Math.min(dp[i + 1][1], dp[i][j] + 1 + cost[i]);
                    dp[i + 1][0] = Math.min(dp[i + 1][0], dp[i][j] + cost[i]);
                } else {
                    dp[i + 1][1] = Math.min(dp[i + 1][1], dp[i][j] + cost[i + 1]);
                    dp[i + 1][0] = Math.min(dp[i + 1][0], dp[i][j]);
                }


            }
        }
        System.out.println(Arrays.deepToString(dp));

        long ans = dp[39][0];
        return (int) ans;
    }
}

