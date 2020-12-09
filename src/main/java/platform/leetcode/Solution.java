package platform.leetcode;


import java.util.Arrays;

public class Solution {
    public int minimumIncompatibility(int[] nums, int k) {
        int n = nums.length;
        for(int i = 0; i < n; i++){
            nums[i]--;
        }

        int inf = (int)1e8;
        int[] cost = new int[1 << n];
        Arrays.fill(cost, inf);
        for(int i = 0; i < 1 << n; i++){
            if(Integer.bitCount(i) != 4){
                continue;
            }
            int min = n + 1;
            int max = -1;
            int v = 0;
            for(int j = 0; j < n; j++){
                if(((i >> j) & 1) == 1){
                    v |= 1 << j;
                    min = Math.min(min, j);
                    max = Math.max(max, j);
                }
            }
            if(Integer.bitCount(v) != 4){
                continue;
            }
            cost[i] = max - min;
        }

        int[] dp = new int[1 << n];
        Arrays.fill(dp, inf);
        dp[0] = 0;
        for(int i = 0; i < 1 << n; i++){
            int set = (1 << n) - 1 - i;
            int now = set + 1;
            do{
                now = (now - 1) & set;
                dp[i + now] = Math.min(dp[i + now], dp[i] + cost[now]);
            }while (now != 0);
        }
        return dp[dp.length - 1];
    }
}

