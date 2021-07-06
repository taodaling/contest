package on2021_06.on2021_06_24_Luogu.P4859____________;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class P4859 {
    int mod = (int) 1e9 + 9;
    Factorial fact = new Factorial(10000, mod);
    Combination comb = new Combination(fact);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        debug.debugArray("a", a);
        debug.debugArray("b", b);
        //win - lose = k
        //win + lost = n
        if ((k + n) % 2 == 1) {
            out.println(0);
            return;
        }
        int win = (k + n) / 2;
        debug.debug("win", win);
        long[] prev = new long[n + 1];
        long[] next = new long[n + 1];
        prev[0] = 1;
        for (int i = 0, head = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            Arrays.fill(next, 0);
            while (head < n && b[head] < a[i]) {
                head++;
            }
            for (int j = 0; j <= n; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                int choice = head - j;
                //add
                if (j + 1 < n) {
                    next[j + 1] += choice * prev[j] % mod;
                }
                //not add
                next[j] += prev[j];
            }
            for (int j = 0; j <= n; j++) {
                next[j] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.debug("prev", prev);
        long[] alpha = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            alpha[i] = prev[i] * fact.fact(n - i) % mod;
        }
        debug.debug("alpha", alpha);
        long ans = 0;
        for (int i = win; i <= n; i++) {
            long contrib = comb.combination(i, win) * alpha[i] % mod;
            if ((i - win) % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
