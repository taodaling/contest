package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class CSequenceScores {
    int mod = 998244353;
    Debug debug = new Debug(false);
    public long calc(int n, int m, int t) {
        debug.debug("t", t);
        long[] sumPrev = new long[2];
        long[] sumNext = new long[2];
        long[] wayPrev = new long[2];
        long[] wayNext = new long[2];
        wayPrev[1] = 1;
        sumPrev[1] = 0;

        int high = m - t;
        int low = t - 1;
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("sumPrev", sumPrev);
            debug.debug("wayPrev", wayPrev);
            Arrays.fill(sumNext, 0);
            Arrays.fill(wayNext, 0);
            for (int j = 0; j < 2; j++) {
                //set 1..low
                wayNext[1] += wayPrev[j] * low;
                sumNext[1] += sumPrev[j] * low;

                //set t+1..high
                wayNext[j] += wayPrev[j] * high;
                sumNext[j] += sumPrev[j] * high;

                //set t
                wayNext[0] += wayPrev[j];
                sumNext[0] += sumPrev[j] + wayPrev[j] * j;
            }

            for (int j = 0; j < 2; j++) {
                wayNext[j] %= mod;
                sumNext[j] %= mod;
            }

            long[] tmp = sumPrev;
            sumPrev = sumNext;
            sumNext = tmp;

            tmp = wayPrev;
            wayPrev = wayNext;
            wayNext = tmp;
        }

        debug.debug("i", n);
        debug.debug("sumPrev", sumPrev);
        debug.debug("wayPrev", wayPrev);
        long ans = (sumPrev[0] + sumPrev[1]) % mod;
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long ans = 0;
        for (int i = 1; i <= m; i++) {
            ans += calc(n, m, i);
        }
        ans %= mod;
        out.println(ans);
    }
}
