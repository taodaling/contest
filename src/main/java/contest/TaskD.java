package contest;

import template.datastructure.BIT;
import template.datastructure.DiscreteMap;
import template.datastructure.IntHashMap;
import template.datastructure.LongHashMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int l = 1;
        int r = (int) 1e9;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (check(a, m)) {
                l = m;
            } else {
                r = m - 1;
            }
        }

        out.println(l);
    }

    public boolean check(int[] a, int m) {
        int n = a.length;
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = a[i] >= m ? 1 : -1;
        }
        long total = 0;
        BIT bit = new BIT(2 * n + 1);
        int zero = n + 1;
        int preSum = 0;
        bit.update(zero, 1);
        for (int i = 0; i < n; i++) {
            preSum += b[i];
            int lessThan = preSum;
            total += bit.query(zero + lessThan);
            bit.update(zero + preSum, 1);
        }
        long all = (long) (1 + n) * n / 2;
        return all - total < all / 2 + 1;
    }
}
