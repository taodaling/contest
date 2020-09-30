package on2020_08.on2020_08_14_TopCoder_SRM__740.DieDesign;



import template.utils.SequenceUtils;

import java.util.Arrays;

public class DieDesign {

    public int[] design(int[] pips) {
        Arrays.sort(pips);
        int n = pips.length;
        int sum = Arrays.stream(pips).sum();

        int[][] dp = new int[n + 1][sum + 1];
        int[][] pre = new int[n + 1][sum + 1];
        SequenceUtils.deepFill(dp, -(int) 1e9);

        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= sum; j++) {
                if (dp[i + 1][j] < dp[i][j]) {
                    dp[i + 1][j] = dp[i][j];
                    pre[i + 1][j] = 0;
                }
                for (int k = 0; k < n && j + pips[k] + 1 <= sum; k++) {
                    int next = j + pips[k] + 1;
                    if (dp[i + 1][next] < dp[i][j] + (k + 1)) {
                        dp[i + 1][next] = dp[i][j] + (k + 1);
                        pre[i + 1][next] = pips[k] + 1;
                    }
                }
            }
        }

        int used = 0;
        for (int i = 0; i <= sum; i++) {
            if (dp[n][i] > dp[n][used]) {
                used = i;
            }
        }

        int[] ans = new int[n];
        for (int i = n; i >= 1; i--) {
            ans[i - 1] = pre[i][used];
            used -= pre[i][used];
        }

        ans[0] += sum - Arrays.stream(ans).sum();

        return ans;
    }
}
