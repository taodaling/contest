package contest;

import template.algo.LIS;
import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class EMakeItIncreasing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for(int i = 0; i < n; i++){
            a[i] -= i;
        }
        int[] b = new int[k];
        in.populate(b);
        for (int i = 0; i < k; i++) {
            b[i]--;
        }
        if (k == 0) {
            out.println(n - lis(a, 0, n - 1, false, false));
            return;
        }
        Randomized.shuffle(b);
        Arrays.sort(b);
        for (int i = 1; i < k; i++) {
            if (a[b[i - 1]] > a[b[i]]) {
                out.println(-1);
                return;
            }
        }
        int lis = lis(a, 0, b[0], false, true) - 1;
        for (int i = 1; i < k; i++) {
            lis += lis(a, b[i - 1], b[i], true, true) - 2;
        }
        lis += lis(a, b[k - 1], n - 1, true, false) - 1;
        out.println(n - k - lis);
    }

    Debug debug = new Debug(true);
    public int lis(int[] a, int l, int r, boolean includeL, boolean includeR) {
        MultiSet<Integer> set = new MultiSet<>();
        set.add(a[l]);
        for (int i = l + 1; i <= r; i++) {
            if (includeL && a[i] < a[l]) {
                continue;
            }
            Integer ceil = set.ceil(a[i] + 1);
            if (ceil != null) {
                set.remove(ceil);
            }
            set.add(a[i]);
        }
        if (includeR) {
            while (set.last() > a[r]) {
                set.pollLast();
            }
        }
        debug.run(() -> {
            String s = String.format("[%d, %d], %d %d = %d", l, r, includeL ? 1 : 0, includeR ? 1 : 0, set.size());
            debug.debug("res", s);
        });
        return set.size();
    }
}
