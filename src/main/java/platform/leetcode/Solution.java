package platform.leetcode;


import template.utils.Debug;

import java.util.Arrays;

public class Solution {
    public static void main(String[] args) {
        System.out.println(new Solution().getMaxGridHappiness(2, 2, 4, 0));
    }

    int[] pow3 = new int[10];

    {
        pow3[0] = 1;
        for (int i = 1; i < 10; i++) {
            pow3[i] = 3 * pow3[i - 1];
        }
    }

    public int get(int x, int i) {
        return x % pow3[i + 1] / pow3[i];
    }

    public int set(int x, int i, int v) {
        return x + (v - get(x, i)) * pow3[i];
    }


    public int getMaxGridHappiness(int m, int n, int a, int b) {
        int p = pow3[n];
        int[][][][] dp = new int[n * (m + 1)][a + 1][b + 1][p];
        int inf = (int) 1e9;
        for (int[][][] r3 : dp) {
            for (int[][] r2 : r3) {
                for (int[] r1 : r2) {
                    Arrays.fill(r1, -inf);
                }
            }
        }
        dp[n - 1][0][0][0] = 0;
        int[][] profit = new int[][]{
                {0, 0, 0},
                {0, -60, -10},
                {0, -10, 40}
        };
        for (int i = n - 1; i + 1 < n * (m + 1); i++) {
            int c = i % n;
            int nc = (c + 1) % n;
            for (int j = 0; j <= a; j++) {
                for (int k = 0; k <= b; k++) {
                    for (int t = 0; t < p; t++) {
                        //nothing
                        dp[i + 1][j][k][set(t, nc, 0)] = Math.max(dp[i + 1][j][k][set(t, nc, 0)],
                                dp[i][j][k][t]);
                        //set 1
                        if (j + 1 <= a) {
                            int cand = dp[i][j][k][t] + 120;
                            if (nc > 0) {
                                cand += profit[1][get(t, nc - 1)];
                            }
                            cand += profit[1][get(t, nc)];
                            dp[i + 1][j + 1][k][set(t, nc, 1)] = Math.max(dp[i + 1][j + 1][k][set(t, nc, 1)],
                                    cand);
                        }

                        //set 2
                        if (k + 1 <= b) {
                            int cand = dp[i][j][k][t] + 40;
                            if (nc > 0) {
                                cand += profit[2][get(t, nc - 1)];
                            }
                            cand += profit[2][get(t, nc)];
                            dp[i + 1][j][k + 1][set(t, nc, 2)] = Math.max(dp[i + 1][j][k + 1][set(t, nc, 2)],
                                    cand);
                        }
                    }
                }
            }
        }

        int ans = -1;
        for (int i = 0; i < p; i++) {
            for (int j = 0; j <= a; j++) {
                for (int k = 0; k <= b; k++) {
                    ans = Math.max(ans, dp[n * (m + 1) - 1][j][k][i]);
                }
            }
        }

        return ans;
    }
}

