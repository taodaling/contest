package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;

public class ESegmentsOnTheLine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Interval[] intervals = new Interval[s];

        int total = 0;
        for (int i = 0; i < s; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt() - 1;
            intervals[i].r = in.readInt() - 1;
            total += intervals[i].r - intervals[i].l + 1;
        }

        if (total < k) {
            out.println(-1);
            return;
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                for (Interval interval : intervals) {
                    interval.w = 0;
                    for (int i = interval.l; i <= interval.r; i++) {
                        if (a[i] <= mid) {
                            interval.w++;
                        }
                    }
                }
                Arrays.sort(intervals, (a, b) -> -Integer.compare(a.w, b.w));
                int count = 0;
                for (int i = 0; i < m; i++) {
                    count += intervals[i].w;
                }

                return count >= k;
            }
        };

        int ans = ibs.binarySearch(1, (int) 1e9);
        out.println(ans);
    }
}

class Interval {
    int l;
    int r;
    int w;
}
