package on2021_04.on2021_04_19_AtCoder___AtCoder_Regular_Contest_117.F___Gateau;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public class FGateau {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n * 2);
        Interval[] intervals = new Interval[n * 2];
        for (int i = 0; i < n + 1; i++) {
            intervals[i] = new Interval(i, i + n - 1, a[i]);
        }
        for (int i = n + 1; i < n * 2; i++) {
            intervals[i] = new Interval(i - n, i - 1, 0);
        }
        Interval[] sorted = intervals.clone();
        Arrays.sort(sorted, Comparator.comparingInt(x -> x.r));

        long[] ps = new long[n * 2];
        IntPredicate predicate = mid -> {
            for (int i = n + 1; i < n * 2; i++) {
                intervals[i].sum = mid - a[i];
            }
            Arrays.fill(ps, 0);
            for (int i = 0, tail = 0; i < 2 * n; i++) {
                long atleast = 0;
                while (tail < 2 * n && sorted[tail].r == i) {
                    atleast = Math.max(atleast, sorted[tail].sum - get(ps, sorted[tail].l, sorted[tail].r - 1));
                    tail++;
                }
                ps[i] = atleast;
                if (i > 0) {
                    ps[i] += ps[i - 1];
                }
            }
            return ps[n * 2 - 1] <= mid;
        };

        predicate.test(7);
        int ans = BinarySearch.firstTrue(predicate, 0, (int) 1e9);
        out.println(ans);
    }

    public long get(long[] ps, int l, int r) {
        long ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        return ans;
    }
}

class Interval {
    int l;
    int r;
    long sum;

    public Interval(int l, int r, long sum) {
        this.l = l;
        this.r = r;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "l=" + l +
                ", r=" + r +
                ", sum=" + sum +
                '}';
    }
}
