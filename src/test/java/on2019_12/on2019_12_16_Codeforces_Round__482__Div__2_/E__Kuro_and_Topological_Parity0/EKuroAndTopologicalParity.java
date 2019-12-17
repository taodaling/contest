package on2019_12.on2019_12_16_Codeforces_Round__482__Div__2_.E__Kuro_and_Topological_Parity0;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;

public class EKuroAndTopologicalParity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();

        Modular mod = new Modular(1e9 + 7);
        CachedPow pow = new CachedPow(2, n, mod);

        int[][][][] dp = new int[n + 1][2][2][2];
        dp[0][0][0][0] = 1;

        for (int i = 1; i <= n; i++) {
            int c = in.readInt();

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t < 2; t++) {
                        int cnt = i - 2 >= 0 ? pow.pow(i - 2) : 1;
                        if (c != 1) {
                            if (t == 1) {
                                //pick odd
                                dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(dp[i - 1][j][k][t], cnt));
                                if (k == 1) {
                                    //pick even
                                    dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(mod.plus(dp[i - 1][1 - j][1][t], dp[i - 1][1 - j][0][t]), cnt));
                                }
                            } else {
                                if (k == 1) {
                                    dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(mod.plus(dp[i - 1][1 - j][0][t], dp[i - 1][1 - j][1][t]), pow.pow(i - 1)));
                                }
                            }
                        }
                        if (c != 0) {
                            //pick odd
                            if (k == 1) {
                                dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(dp[i - 1][j][k][t], cnt));
                                if (t == 1) {
                                    //pick even
                                    dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(mod.plus(dp[i - 1][1 - j][k][1], dp[i - 1][1 - j][k][0]), cnt));
                                }
                            } else {
                                if (t == 1) {
                                    //pick even
                                    dp[i][j][k][t] = mod.plus(dp[i][j][k][t], mod.mul(mod.plus(dp[i - 1][1 - j][k][1], dp[i - 1][1 - j][k][0]), pow.pow(i - 1)));
                                }
                            }

                        }
                    }
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ans = mod.plus(dp[n][p][i][j], ans);
            }
        }

        out.println(ans);
    }
}
