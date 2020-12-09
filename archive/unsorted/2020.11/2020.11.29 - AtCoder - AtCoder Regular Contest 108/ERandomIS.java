package contest;

import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class ERandomIS {
    int mod = (int) 1e9 + 7;
    InverseNumber inv = new ModPrimeInverseNumber((int) 1e4, mod);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        a[0] = 0;
        a[n + 1] = n + 1;

        ModBIT[] left = new ModBIT[n + 2];
        ModBIT[] right = new ModBIT[n + 2];
        for (int i = 0; i < n + 2; i++) {
            left[i] = new ModBIT(n + 2, mod);
            right[i] = new ModBIT(n + 2, mod);
        }
        int[][] between = new int[n + 2][n + 2];
        IntegerBIT bit = new IntegerBIT(n + 2);
        for (int i = 0; i < n + 2; i++) {
            bit.clear();
            for (int j = i + 1; j < n + 2; j++) {
                if (a[i] < a[j]) {
                    between[i][j] = bit.query(a[i], a[j]);
                    bit.update(a[j], 1);
                }
            }
        }

        update(left[0], n + 1, n + 1, 1);
        List<Interval> intervals = new ArrayList<>(n * n);
        for (int i = 0; i <= n + 1; i++) {
            for (int j = i + 1; j <= n + 1; j++) {
                if (a[i] < a[j]) {
                    intervals.add(new Interval(i, j, a[j] - a[i]));
                }
            }
        }
        intervals.sort((x, y) -> -Integer.compare(x.delta, y.delta));
        long[] exp = new long[n + 2];
        for (Interval interval : intervals) {
            int l = interval.l;
            int r = interval.r;
            int prob = DigitUtils.modplus(query(left[l], r), query(right[r], l), mod);
            debug.debug("interval", interval);
            debug.debug("prob", prob);
            if (prob == 0) {
                continue;
            }
            int b = between[l][r];
            if (b == 0) {
                //update(right[r], r, r, prob);
                exp[r] += prob;
                continue;
            }
            prob = (int) ((long) prob * inv.inverse(b) % mod);
            update(left[l], l + 1, r - 1, prob);
            update(right[r], l + 1, r - 1, prob);
        }

        debug.debugArray("exp", exp);
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            //assert exp[i] % mod == (query(left[i], i) + query(right[i], i)) % mod;
            sum += exp[i];
        }
        sum %= mod;
        out.println(sum);
    }

    public void update(ModBIT bit, int l, int r, int v) {
        l++;
        r++;
        bit.update(l, v);
        bit.update(r + 1, -v);
    }

    public int query(ModBIT bit, int i) {
        i++;
        return bit.query(i);
    }
}

class Interval {
    int l;
    int r;
    int delta;

    public Interval(int l, int r, int delta) {
        this.l = l;
        this.r = r;
        this.delta = delta;
    }

}
