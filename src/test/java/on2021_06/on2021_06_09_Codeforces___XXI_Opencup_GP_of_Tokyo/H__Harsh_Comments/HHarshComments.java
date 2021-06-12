package on2021_06.on2021_06_09_Codeforces___XXI_Opencup_GP_of_Tokyo.H__Harsh_Comments;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.Power;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class HHarshComments {
    int mod = 998244353;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int s = Arrays.stream(a).sum();
        int[] b = in.ri(m);

        long[][] prev = new long[2][s + 1];
        long[][] next = new long[2][s + 1];
        prev[0][0] = 1;
        for (int x : a) {
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k <= s; k++) {
                    if (prev[j][k] == 0) {
                        continue;
                    }
                    //add
                    next[j ^ 1][k + x] += prev[j][k];
                    //or not
                    next[j][k] += prev[j][k];
                }
            }

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k <= s; k++) {
                    next[j][k] = DigitUtils.modWithoutDivision(next[j][k], mod);
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        debug.debug("prev", prev);
        long exp = n;
        for (int x : b) {
            long totalProb = 0;
            for (int j = 1; j <= s; j++) {
                long prob = (long) x * pow.inverse(x + j) % mod;
                long contrib = prob * (prev[1][j] - prev[0][j]) % mod;
                totalProb += contrib;
            }

            debug.debug("x", x);
            debug.debug("totalProb", totalProb);
            exp += totalProb;
        }

        exp = DigitUtils.mod(exp, mod);
        out.println(exp);
    }

    Debug debug = new Debug(false);
}
