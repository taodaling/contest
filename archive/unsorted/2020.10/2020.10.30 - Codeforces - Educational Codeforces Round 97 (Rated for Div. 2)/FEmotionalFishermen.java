package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.utils.Debug;

import java.util.Arrays;

public class FEmotionalFishermen {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        int[] prev = new int[n + 1];
        in.populate(a);
        Arrays.sort(a);
        if (a[n] < 2 * a[n - 1]) {
            out.println(0);
            return;
        }
        for (int i = 1, l = 0; i <= n; i++) {
            while (l <= n && a[l] * 2 <= a[i]) {
                l++;
            }
            prev[i] = l;
        }
        int mod = 998244353;
        Factorial fact = new Factorial(10000, mod);
        Combination comb = new Combination(10000, mod);

        long[] last = new long[n + 1];
        long[] next = new long[n + 1];
        last[0] = 1;
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, 0);
            for (int j = 0; j <= i; j++) {
                if (last[j] == 0) {
                    continue;
                }
                //use it
                if (a[i + 1] >= 2 * a[j]) {
                    next[i + 1] += last[j] * comb.combination(n + 1 - (1 + prev[j]) - 1, prev[i + 1] - prev[j] - 1) % mod *
                            fact.fact(prev[i + 1] - prev[j] - 1) % mod;
                }
                next[j] += last[j];
            }
            long[] tmp = last;
            last = next;
            next = tmp;
            for(int j = 0; j <= n; j++){
                last[j] %= mod;
            }
        }

        long ans = last[n];
        out.println(ans);
    }
}
