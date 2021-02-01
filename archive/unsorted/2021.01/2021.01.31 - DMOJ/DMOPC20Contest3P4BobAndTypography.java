package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DMOPC20Contest3P4BobAndTypography {
    int inf = (int) 1e9;

    int[][] solve(int[] a) {
        int n = a.length;
        int[][] dp = new int[n][n];
        SequenceUtils.deepFill(dp, -inf);
        for (int i = 0; i < n; i++) {
            int A = 0;
            int B = a[i];
            dp[i][0] = 1;
            for (int j = i, z = i; j >= 1; j--) {
                B -= a[j];
                A += a[j];
                while (B < A && z > 0) {
                    z--;
                    B += a[z];
                }
                if (B < A) {
                    break;
                }
                dp[i][j] = Math.max(dp[i][j], dp[j - 1][z] + 1);
            }
            for (int j = 1; j <= i; j++) {
                dp[i][j] = Math.max(dp[i][j], dp[i][j - 1]);
            }
        }
        return dp;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        int[][] front = solve(a);
        SequenceUtils.reverse(a);
        int[][] back = solve(a);
        int ans = 1;
        for (int i = 0; i + 1 < n; i++) {
            int l = i;
            int r = n - 1 - (i + 1);
            int sum = front[l][l] + back[r][r];
            ans = Math.max(ans, sum);
        }
        out.println(ans);
    }
}
