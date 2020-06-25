package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.Debug;

public class EMedianReplace {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 3e5];
        int n = in.readString(s, 0);
        int[][][] dp = new int[n][3][3];

        Modular mod = new Modular(1e9 + 7);
        if (s[0] == '1' || s[0] == '?') {
            dp[0][1][0]++;
        }
        if (s[0] == '0' || s[0] == '?') {
            dp[0][0][1]++;
        }

        for (int i = 0; i < n - 1; i++) {
            int l = s[i + 1] != '1' ? 0 : 1;
            int r = s[i + 1] != '0' ? 1 : 0;
            for (int j = l; j <= r; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int t = 0; t < 3; t++) {
                        int nk = k;
                        int nt = t;
                        if (j == 0) {
                            nt++;
                        } else if (nt == 0) {
                            nk++;
                        } else {
                            nt--;
                        }

                        if (nt >= 3) {
                            nt -= 2;
                        }
                        if (nk >= 3) {
                            nk = 2;
                        }

                        dp[i + 1][nk][nt] = mod.plus(dp[i + 1][nk][nt], dp[i][k][t]);
                    }
                }
            }
        }


        debug.debug("dp", dp);

        int ans = dp[n - 1][1][0];
        for (int i = 0; i < 3; i++) {
            ans = mod.plus(ans, dp[n - 1][2][i]);
        }
        out.println(ans);
    }
}
