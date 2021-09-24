package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Sum_of_Squares;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;

public class SumOfSquares {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] Tprev = new long[n + 1];
        long[] Tnext = new long[n + 1];
        long[] Sprev = new long[n + 1];
        long[] Snext = new long[n + 1];
        long[] Wprev = new long[n + 1];
        long[] Wnext = new long[n + 1];
        Wprev[0] = 1;
        for (int i = 1; i <= k; i++) {
            Arrays.fill(Tnext, 0);
            Arrays.fill(Snext, 0);
            Arrays.fill(Wnext, 0);
            for (int start = 0; start < i; start++) {
                long sumS = 0;
                long sumWay = 0;
                long sumT = 0;
                long sumWayJ = 0;
                long sumWayJJ = 0;
                long sumTJ = 0;
                int t;
                for (int j = 0; (t = start + j * i) <= n; j++) {
                    if (i == k) {
                        Wnext[t] += sumWay;
                        Tnext[t] += (sumT + (sumWay * j - sumWayJ) * i) % mod;
                        Snext[t] += (sumS + (sumWay * j * j + sumWayJJ - 2 * sumWayJ * j) % mod * i % mod + (sumT * j - sumTJ) * 2) % mod;
                    }
                    sumTJ += Tprev[t] * j % mod;
                    sumWayJ += Wprev[t] * j % mod;
                    sumWayJJ += Wprev[t] * j * j % mod;
                    sumWay += Wprev[t];
                    sumT += Tprev[t];
                    sumS += Sprev[t];
                    if (sumTJ >= mod) {
                        sumTJ -= mod;
                    }
                    if (sumWayJJ >= mod) {
                        sumWayJJ -= mod;
                    }
                    if (sumWayJ >= mod) {
                        sumWay -= mod;
                    }
                    if (sumWay >= mod) {
                        sumWay -= mod;
                    }
                    if (sumT >= mod) {
                        sumT -= mod;
                    }
                    if (sumS >= mod) {
                        sumS -= mod;
                    }
                    if (!(i == k)) {
                        Wnext[t] += sumWay;
                        Tnext[t] += (sumT + (sumWay * j - sumWayJ) * i) % mod;
                        Snext[t] += (sumS + (sumWay * j * j + sumWayJJ - 2 * sumWayJ * j) % mod * i % mod + (sumT * j - sumTJ) * 2) % mod;
                    }
                }
            }
            for (int j = 0; j <= n; j++) {
                Tnext[j] = DigitUtils.modWithoutDivision(Tnext[j], mod);
                Snext[j] = DigitUtils.modWithoutDivision(Snext[j], mod);
                Wnext[j] = DigitUtils.modWithoutDivision(Wnext[j], mod);
            }
            long[] tmp = Tprev;
            Tprev = Tnext;
            Tnext = tmp;
            tmp = Sprev;
            Sprev = Snext;
            Snext = tmp;
            tmp = Wprev;
            Wprev = Wnext;
            Wnext = tmp;

            debug.debug("i", i);
            debug.debug("Wprev", Wprev);
            debug.debug("Sprev", Sprev);
            debug.debug("Tprev", Tprev);
        }

        long ans = Sprev[n];
        out.println(ans);
    }
}
