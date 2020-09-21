package platform.leetcode;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Solution {
    public int connectTwoGroups(List<List<Integer>> cost) {
        int n = cost.size();
        int m = cost.get(0).size();
        int[][] dp = new int[n + 1][1 << m];
        int inf = (int) 1e9;
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], inf);
        }
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            List<Integer> prices = cost.get(i);
            for (int j = 0; j < 1 << m; j++) {
                for (int k = 0; k < m; k++) {
                    int transfer = j | (1 << k);
                    dp[i + 1][transfer] = Math.min(dp[i + 1][transfer], dp[i][j] + prices.get(k));
                }
            }
        }
        int[] minCost = new int[1 << m];
        Arrays.fill(minCost, inf);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                minCost[j] = Math.min(minCost[j], cost.get(i).get(j));
            }
        }

        int ans = inf;
        for(int i = 0; i < 1 << m; i++){
            int delta = (1 << m) - 1 - i;
            int can = dp[n][i];
            for(int j = 0; j < m; j++){
                if((delta & (1 << j)) != 0){
                    can += minCost[j];
                }
            }
            ans = Math.min(ans, can);
        }

        return ans;
    }
}

