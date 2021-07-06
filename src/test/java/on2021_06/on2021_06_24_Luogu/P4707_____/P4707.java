package on2021_06.on2021_06_24_Luogu.P4707_____;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class P4707 {
    int mod = 998244353;
    InverseNumber inv = new ModPrimeInverseNumber((int) 2e4, mod);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int m = in.ri();
        int[] p = in.ri(n);
        p = Arrays.stream(p).filter(x -> x != 0).toArray();
        n = p.length;
        int del = n - k;
        int kthLargest = del + 1;
        debug.debug("del", del);
        debug.debug("kthLargest", kthLargest);
        long[][][] prev = new long[2][del + 1][m + 1];
        long[][][] next = new long[2][del + 1][m + 1];
        prev[0][0][0] = 1;
        for (int prob : p) {
            SequenceUtils.deepFill(next, 0L);
            for (int sign = 0; sign < 2; sign++) {
                for (int i = 0; i <= del; i++) {
                    for (int j = 0; j <= m; j++) {
                        if (prev[sign][i][j] == 0) {
                            continue;
                        }
                        //choose, as subset
                        //0, 0
                        next[sign][i][j] += prev[sign][i][j];
                        //1,0
                        next[sign ^ 1][i][j + prob] += prev[sign][i][j];
                        //1,1
                        if (i + 1 <= del && j > 0) {
                            next[sign ^ 1][i + 1][j + prob] += prev[sign][i][j];
                        }
                    }
                }
            }
            for (int sign = 0; sign < 2; sign++) {
                for (int i = 0; i <= del; i++) {
                    for (int j = 0; j <= m; j++) {
                        next[sign][i][j] %= mod;
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }

        long[][][] dp = prev;
        debug.debug("dp", dp);
        long exp = 0;
        for (int sign = 0; sign < 2; sign++) {
            for (int j = 1; j <= m; j++) {
                long contrib = dp[sign][del][j] * m % mod *
                        inv.inverse(j) % mod;
                if (((sign - kthLargest) & 1) == 1) {
                    contrib = -contrib;
                }
                exp += contrib;
            }
        }
        exp = DigitUtils.mod(exp, mod);
        out.println(exp);
    }
}
