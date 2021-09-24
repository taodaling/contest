package on2021_09.on2021_09_05_CS_Academy___Virtual_Round__27.Permutation_Towers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.IntMath;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class PermutationTowers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int mod = in.ri();
        x--;

        Combination comb = new Combination((int) 2e5, mod);

        int sum = (int) IntMath.sumOfInterval(1, n) * 2;
        long[][] prev = new long[n + 3][sum + 1];
        prev[2][0] = 1;
        long[][] next = new long[n + 3][sum + 1];
        int ps = 0;
        for (int e = n; e >= 1; e--) {
            SequenceUtils.deepFill(next, 0L);
            int possible = n - e + 2;
            for (int i = 0; i <= possible; i++) {
                for (int j = 0; j <= ps; j++) {
                    long way = prev[i][j];
                    if (way == 0) {
                        continue;
                    }
                    //add one
                    if (i + 1 <= n) {
                        next[i + 1][j + e * 2] += way * (i - 1) % mod;
                    }

                    //merge into
                    if (i > 0) {
                        next[i][j + e] += way * (i * 2 - 2) % mod;
                    }
                    //merge two
                    if (i >= 2) {
                        next[i - 1][j] += way * (i - 1) % mod;
                    }
                }
            }
            for (int i = 0; i <= n; i++) {
                for (int j = 0; j <= sum; j++) {
                    next[i][j] = DigitUtils.modWithoutDivision(next[i][j], mod);
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
            debug.debug("e", e);
            debug.debug("prev", prev);
            ps += 2 * e;
        }
        long ans = 0;
        for (int i = 0; i <= sum; i++) {
            long way = prev[1][i];
            if (way == 0) {
                continue;
            }
            debug.debug("i", i);
            debug.debug("way", way);
            //d2+...+dn-1 = i
            //d1 >= 0
            //dn >= 0
            //d1 + ... + dn = x
            if (i > x) {
                continue;
            }
            long contrib = comb.combination(x - i + n, n) * way % mod;
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
