package on2021_07.on2021_07_16_DMOJ___Wesley_s_Anger_Contest_5.Problem_5___A_Squirrel_s_Life;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class Problem5ASquirrelsLife {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int e = in.ri();
        int k = in.ri();
        int L = 3000;
        long[][][][][] dp = new long[L][2][2][k + 2][n + 1];
        dp[0][0][0][0][0] = 1;
        int[][] transfer = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                transfer[i][j] = in.rc() - '0';
            }
        }
        transfer[0][1] = 1;

        for (int i = 0; i + 1 < L; i++) {
            for (int j = 0; j < 2; j++) {
                for (int t = 0; t < 2; t++) {
                    for (int z = 0; z < k + 2; z++) {
                        for (int x = 0; x <= n; x++) {
                            long way = dp[i][j][t][z][x];
                            if (way == 0) {
                                continue;
                            }
                            for (int to = 1; to <= n; to++) {
                                if (transfer[x][to] != 1) {
                                    continue;
                                }
                                if (to == 2) {
                                    dp[i + 1][1][t][z][to] += way;
                                } else if (to == 3) {
                                    dp[i + 1][j][1][z][to] += way;
                                } else if (to == 4) {
                                    dp[i + 1][j][t][Math.min(k + 1, z + 1)][to] += way;
                                } else {
                                    dp[i + 1][j][t][z][to] += way;
                                }
                            }
                        }
                    }
                }
            }
            for (int j = 0; j < 2; j++) {
                for (int t = 0; t < 2; t++) {
                    for (int z = 0; z < k + 2; z++) {
                        for (int x = 0; x < n + 1; x++) {
                            dp[i + 1][j][t][z][x] %= mod;
                        }
                    }
                }
            }
        }
        IntegerArrayList prefix = new IntegerArrayList(L);
        for (int i = 0; i < L; i++) {
            prefix.add((int) dp[i][1][1][k][n]);
        }
        debug.debug("prefix", prefix);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(prefix, mod, new IntPolyNTT(mod));
        int ans = solver.solve(e, prefix);
        out.println(ans);
    }
    Debug debug = new Debug(false);
}
