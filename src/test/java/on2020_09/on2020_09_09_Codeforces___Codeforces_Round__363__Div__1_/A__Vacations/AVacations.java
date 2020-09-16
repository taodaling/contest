package on2020_09.on2020_09_09_Codeforces___Codeforces_Round__363__Div__1_.A__Vacations;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class AVacations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[][] dp = new int[n + 1][3];
        SequenceUtils.deepFill(dp, (int) 1e8);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (j == k && j != 0) {
                        continue;
                    }
                    if ((k & a[i]) != k) {
                        continue;
                    }
                    dp[i + 1][k] = Math.min(dp[i + 1][k], dp[i][j] + (k == 0 ? 1 : 0));
                }
            }
        }
        int ans = (int) 1e8;
        for (int x : dp[n]) {
            ans = Math.min(ans, x);
        }
        out.println(ans);
    }
}
