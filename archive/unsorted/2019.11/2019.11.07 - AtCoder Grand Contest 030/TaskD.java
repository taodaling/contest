package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[][] swaps = new int[q][2];
        for (int i = 0; i < q; i++) {
            swaps[i][0] = in.readInt() - 1;
            swaps[i][1] = in.readInt() - 1;
        }



        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        int inv2 = pow.inverse(2);

        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i] < a[j]) {
                    dp[i][j]++;
                }
            }
        }

        int[] xInL = new int[n];
        int[] xInR = new int[n];

        for (int j = 0; j < q; j++) {
            int x = swaps[j][0];
            int y = swaps[j][1];

            // x in l
            for (int r = 0; r < n; r++) {
                if (r == y || r == x) {
                    xInL[r] = 0;
                    continue;
                }
                xInL[r] = mod.plus(dp[y][r], dp[x][r]);
            }


            // x in r
            for (int l = 0; l < n; l++) {
                if (l == y || l == x) {
                    xInR[l] = 0;
                    continue;
                }
                xInR[l] = mod.plus(dp[l][x], dp[l][y]);
            }

            int xy = dp[x][y];
            int yx = dp[y][x];

            // update x in l
            for (int i = 0; i < n; i++) {
                int half = mod.mul(inv2, xInL[i]);
                dp[x][i] = dp[y][i] = half;
            }

            // update x in r
            for (int i = 0; i < n; i++) {
                int half = mod.mul(inv2, xInR[i]);
                dp[i][x] = dp[i][y] = half;
            }

            int half = mod.mul(mod.plus(xy, yx), inv2);
            dp[x][y] = dp[y][x] = half;
        }

        int p = pow.pow(2, q);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    ans = mod.plus(dp[i][j], ans);
                }
            }
        }
        ans = mod.mul(p, ans);


        out.println(ans);
    }
}
