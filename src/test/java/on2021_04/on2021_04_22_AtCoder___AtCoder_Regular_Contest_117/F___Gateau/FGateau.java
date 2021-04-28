package on2021_04.on2021_04_22_AtCoder___AtCoder_Regular_Contest_117.F___Gateau;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.LongPredicate;

public class FGateau {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int[] a = in.ri(n * 2);
        intervals = new Interval[n];
        for(int i = 0; i < n; i++){
            intervals[i] = new Interval();
        }
        s = new long[n * 2 + 1];
        LongPredicate predicate = m -> {
            for (int i = 0; i < n; i++) {
                intervals[i].l = a[i];
            }
            for (int i = n; i < 2 * n; i++) {
                intervals[i - n].r = m - a[i];
            }
            for(int i = 0; i < n; i++){
                if(intervals[i].l > intervals[i].r){
                    return false;
                }
            }

            LongPredicate searchForLeftBound = sn -> {
                calc(0, sn);
                return s[n] >= s[n - 1];
            };
            LongPredicate searchForRightBound = sn -> {
                calc(0, sn);
                return s[n * 2 - 1] <= m;
            };

            long L = BinarySearch.firstTrue(searchForLeftBound, intervals[0].l, intervals[0].r);
            long R = BinarySearch.lastTrue(searchForRightBound, intervals[0].l, intervals[0].r);
            return L <= R;
        };

        long ans = BinarySearch.firstTrue(predicate, 0, (long) 2e9);
        out.println(ans);
    }

    int n;
    long[] s;
    Interval[] intervals;

    public void calc(long s0, long sn) {
        s[0] = s0;
        s[n] = sn;
        for (int i = 1; i < n; i++) {
            s[i] = s[i - 1];
            s[i + n] = s[i + n - 1];
            long d = s[i + n] - s[i];
            if (d < intervals[i].l) {
                s[i + n] += intervals[i].l - d;
            } else if (d > intervals[i].r) {
                s[i] += d - intervals[i].r;
            }
        }
    }
}

class Interval {
    long l;
    long r;
}