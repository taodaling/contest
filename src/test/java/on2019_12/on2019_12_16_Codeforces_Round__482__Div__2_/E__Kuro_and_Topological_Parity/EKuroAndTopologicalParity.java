package on2019_12.on2019_12_16_Codeforces_Round__482__Div__2_.E__Kuro_and_Topological_Parity;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Composite;
import template.math.Modular;

public class EKuroAndTopologicalParity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int[] c = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            c[i] = in.readInt();
        }

        Modular mod = new Modular(1e9 + 7);
        Composite comp = new Composite(n, mod);
        CachedPow pow = new CachedPow(2, mod);
        int[] pow2 = new int[n + 1];
        int[] compEven = new int[n + 1];
        int[] compOdd = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j += 2) {
                compEven[i] = mod.plus(compEven[i], comp.composite(i, j));
            }
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= n; j += 2) {
                compOdd[i] = mod.plus(compOdd[i], comp.composite(i, j));
            }
        }
        for (int i = 0; i <= n; i++) {
            pow2[i] = pow.pow(i);
        }

        int[][][][] last = new int[n + 1][n + 1][n + 1][2];
        int[][][][] next = new int[n + 1][n + 1][n + 1][2];

        last[0][0][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    for (int t = j; t + k <= i; t++) {
                        for (int parity = 0; parity < 2; parity++) {
                            next[j][k][t][parity] = 0;
                            if (c[i] != 1 && t > 0) {
                                //it's 0 now

                                //pick even and it's odd
                                if (j > 0) {
                                    next[j][k][t][parity] = mod.plus(next[j][k][t][parity],
                                            mod.mul(mod.mul(last[j - 1][k][t - 1][1 - parity], compEven[k]), pow2[i - t - k]));
                                }

                                //pick odd and it's even
                                next[j][k][t][parity] = mod.plus(next[j][k][t][parity],
                                        mod.mul(mod.mul(last[j][k][t - 1][parity], compOdd[k]), pow2[i - t - k]));
                            }
                            if (c[i] != 0) {
                                //it's 1 now

                                //pick even and it's odd
                                if (k > 0) {
                                    next[j][k][t][parity] = mod.plus(next[j][k][t][parity],
                                            mod.mul(mod.mul(last[j][k - 1][t][1 - parity], compEven[j]), pow2[t - j]));
                                }

                                //pick odd and it's even
                                next[j][k][t][parity] = mod.plus(next[j][k][t][parity],
                                        mod.mul(mod.mul(last[j][k][t][parity], compOdd[j]), pow2[t - j]));
                            }
                        }
                    }
                }
            }

            int[][][][] tmp = last;
            last = next;
            next = tmp;
        }

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    int cnt = last[i][j][k][p];
                    cnt = mod.mul(cnt, pow.pow(comp.composite(k, 2)));
                    cnt = mod.mul(cnt, pow.pow(comp.composite(n - k, 2)));
                    ans = mod.plus(ans, cnt);
                }
            }
        }

        out.println(ans);
    }
}
