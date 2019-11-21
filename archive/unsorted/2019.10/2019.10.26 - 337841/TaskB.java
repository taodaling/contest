package contest;

import template.FastInput;
import template.FastOutput;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int[][] dp = new int[k][n];
        int[][] prefix = new int[k][n];
        dp[0][0] = 1;
        Arrays.fill(prefix[0], 1);
        for (int i = 1; i < k; i++) {
            for (int j = i; j < n; j++) {
                dp[i][j] = prefix[i - 1][j];
            }
            prefix[i][0] = dp[i][0];
            for(int j = 1; j < n; j++){
                prefix[i][j] = mod.plus(prefix[i][j - 1], dp[i][j]);
            }
        }

        int ans = prefix[k - 1][n - 1];
        if(k != n) {
            ans = mod.mul(ans, power.pow(2, n - 1 - (k - 1) - 1));
        }
        out.println(ans);
    }
}
