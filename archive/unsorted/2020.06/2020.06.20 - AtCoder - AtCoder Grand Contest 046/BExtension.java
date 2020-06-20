package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.Debug;

public class BExtension {

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int d = in.readInt();
        int[][][] dp = new int[c + 1][d + 1][2];
        dp[a][b][0] = 1;
        Modular mod = new Modular(998244353);
        for (int i = a; i <= c; i++) {
            for (int j = b; j <= d; j++) {
                for (int k = 0; k < 2; k++) {
                    if (dp[i][j][k] == 0) {
                        continue;
                    }
                    if (k == 0 && j + 1 <= d) {
                        dp[i][j + 1][0] = mod.plus(dp[i][j + 1][0], mod.mul(dp[i][j][k], i));
                    }
                    if (k == 0 && i + 1 <= c) {
                        dp[i + 1][j][1] = mod.plus(dp[i + 1][j][1], mod.mul(dp[i][j][k], j));
                    }
                    if (k == 1 && j + 1 <= d) {
                        dp[i][j + 1][0] = mod.plus(dp[i][j + 1][0], mod.mul(dp[i][j][k], 1));
                    }
                    if (k == 1 && i + 1 <= c) {
                        dp[i + 1][j][1] = mod.plus(dp[i + 1][j][1], mod.mul(dp[i][j][k], j));
                    }
                }
            }
        }

        int ans = mod.plus(dp[c][d][0], dp[c][d][1]);
        out.println(ans);

    }
}
