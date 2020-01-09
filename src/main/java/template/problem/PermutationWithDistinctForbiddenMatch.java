package template.problem;

import template.math.Modular;

import java.util.Arrays;

/**
 * 带禁止位置的排列数
 */
public class PermutationWithDistinctForbiddenMatch {
    private int[][] dp;
    private int[][] dp2;

    public PermutationWithDistinctForbiddenMatch(Modular mod, int n) {
        this.dp = new int[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = mod.mul(dp[i - 1][j], j + j);
                if (j + 1 <= n) {
                    dp[i][j] = mod.plus(dp[i][j], dp[i - 1][j + 1]);
                }
                if (j > 0) {
                    dp[i][j] = mod.plus(dp[i][j], mod.mul(dp[i - 1][j - 1], mod.mul(j, j)));
                }
            }
        }

        dp2 = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (j == 0) {
                    dp2[i][j] = dp[i][j];
                    continue;
                }
                if (j > 0) {
                    dp2[i][j] = mod.plus(dp2[i][j], mod.mul(dp2[i][j - 1], j));
                }
                if (i > 0) {
                    dp2[i][j] = mod.plus(dp2[i][j], mod.mul(dp2[i - 1][j], i));
                }
            }
        }

//        System.err.println(Arrays.deepToString(dp));
//        System.err.println(Arrays.deepToString(dp2));
    }

    /**
     * 排列$i+j$个数，其中对于$k<=i$,第k个数不允许为$k$。
     * @return 方案数
     */
    public int get(int i, int j) {
        return dp2[i][j];
    }
}