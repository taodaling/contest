package on2021_08.on2021_08_05_AtCoder___AtCoder_Beginner_Contest_209.F___Deforestation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class FDeforestation {
    int mod = (int) 1e9 + 7;

    public void update(long[] a, int l, int r, long x) {
        l = Math.max(0, l);
        if (l > r) {
            return;
        }
        a[l] += x;
        if (r + 1 < a.length) {
            a[r + 1] -= x;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long[] prev = new long[n];
        long[] next = new long[n];
        prev[0] = 1;
        for (int r = 0; r + 1 < n; r++) {
            Arrays.fill(next, 0);
            for (int i = 0; i < n; i++) {
                if (prev[i] == 0) {
                    continue;
                }
                if (a[r] < a[r + 1]) {
                    update(next, 0, i, prev[i]);
                } else if (a[r] > a[r + 1]) {
                    update(next, i + 1, r + 1, prev[i]);
                } else {
                    update(next, 0, r + 1, prev[i]);
                }
            }
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    next[i] += next[i - 1];
                }
                next[i] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = Arrays.stream(prev).sum();
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);

    }
}
