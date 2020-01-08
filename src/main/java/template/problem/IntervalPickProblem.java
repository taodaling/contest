package template.problem;

import template.algo.DoubleBinarySearch;
import template.math.DigitUtils;
import template.primitve.generated.LongPreSum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntervalPickProblem {
    public static class Interval {
        public final int l;
        public final int r;
        public boolean used;

        public Interval(int l, int r) {
            this.l = l;
            this.r = r;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", l, r);
        }
    }

    public static Interval[] sortAndRemoveUnusedInterval(Interval[] intervals) {
        Arrays.sort(intervals, (a, b) -> Long.compare(a.l, b.l));
        List<Interval> intervalList = new ArrayList<>(intervals.length);
        int n = 0;
        for (int i = 0; i < intervals.length; i++, n++) {
            int l = i;
            int r = i;
            Interval max = intervals[i];
            while (r + 1 < intervals.length && intervals[r + 1].l == intervals[l].l) {
                r++;
                if (intervals[r].r > max.r) {
                    max = intervals[r];
                }
            }
            i = r;
            intervalList.add(max);
        }
        return intervalList.toArray(new Interval[0]);
    }

    /**
     * 有一个序列data[0], data[1], ... , data[n - 1], 以及m个区间
     * intervals[0], intervals[1], ..., intervals[m - 1].
     * <br>
     * 要求我们选择任意个区间，要求被选中的区间覆盖的元素的和最大。
     * <br>
     */
    public static long solve(long[] data, Interval[] intervals) {
        LongPreSum lps = new LongPreSum(data);
        Arrays.sort(intervals, (a, b) -> a.l == b.l ? a.r - b.r : a.l - b.l);
        int n = intervals.length;
        long[] dp = new long[n];
        int[] last = new int[n];

        LongSegmentQuery query = new LongSegmentQuery();
        int m = data.length;
        LongSegment lower = new LongSegment(0, m);
        LongSegment upper = new LongSegment(0, m);

        for (int i = 0; i < n; i++) {
            Interval now = intervals[i];
            dp[i] = lps.intervalSum(now.l, now.r);
            last[i] = -1;

            query.reset();
            lower.query(0, now.l - 1, 0, m, query);
            if (query.val + lps.intervalSum(now.l, now.r) > dp[i]) {
                dp[i] = query.val + lps.intervalSum(now.l, now.r);
                last[i] = query.index;
            }
            query.reset();
            upper.query(now.l, now.r, 0, m, query);
            if (query.val + lps.prefix(now.r) > dp[i]) {
                dp[i] = query.val + lps.prefix(now.r);
                last[i] = query.index;
            }

            lower.update(now.r, now.r, 0, m, dp[i], i);
            upper.update(now.r, now.r, 0, m, dp[i] - lps.prefix(now.r), i);
        }

        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > dp[maxIndex]) {
                maxIndex = i;
            }
        }

        int trace = maxIndex;
        while (trace >= 0) {
            intervals[trace].used = true;
            trace = last[trace];
        }

        return dp[maxIndex];
    }


    private static class WQSResult {
        double maxValue;
        int time;

        public WQSResult(double maxValue, int time) {
            this.maxValue = maxValue;
            this.time = time;
        }
    }

    private static WQSResult solve(long[] data, Interval[] intervals, double cost) {
        LongPreSum lps = new LongPreSum(data);
        Arrays.sort(intervals, (a, b) -> a.l == b.l ? a.r - b.r : a.l - b.l);
        int n = intervals.length;
        double[] dp = new double[n];
        int[] time = new int[n];

        DoubleSegmentQuery query = new DoubleSegmentQuery();
        int m = data.length;
        DoubleSegment lower = new DoubleSegment(0, m);
        DoubleSegment upper = new DoubleSegment(0, m);

        for (int i = 0; i < n; i++) {
            Interval now = intervals[i];
            dp[i] = lps.intervalSum(now.l, now.r);
            time[i] = 1;

            query.reset();
            lower.query(0, now.l - 1, 0, m, query);
            if (query.val + lps.intervalSum(now.l, now.r) > dp[i]) {
                dp[i] = query.val + lps.intervalSum(now.l, now.r);
                time[i] = time[query.index] + 1;
            }
            query.reset();
            upper.query(now.l, now.r, 0, m, query);
            if (query.val + lps.prefix(now.r) > dp[i]) {
                dp[i] = query.val + lps.prefix(now.r);
                time[i] = time[query.index] + 1;
            }

            dp[i] -= cost;
            lower.update(now.r, now.r, 0, m, dp[i], i);
            upper.update(now.r, now.r, 0, m, dp[i] - lps.prefix(now.r), i);
        }

        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > dp[maxIndex]) {
                maxIndex = i;
            }
        }

        return new WQSResult(dp[maxIndex], time[maxIndex]);
    }

    /**
     * 有一个序列data[0], data[1], ... , data[n - 1], 以及m个区间
     * intervals[0], intervals[1], ..., intervals[m - 1].
     * <br>
     * 要求我们正好选择k个区间，要求被选中的区间覆盖的元素的和最大。
     * <br>
     * 区间需要满足intervals[i].l < intervals[i + 1].l且intervals[i].r < intervals[i + 1].r
     * <br>
     */
    public static long solve(long[] data, Interval[] intervals, int k) {
        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-12, 1e-12) {
            @Override
            public boolean check(double mid) {
                return solve(data, intervals, mid).time <= k;
            }
        };

        long sum = 0;
        for (long x : data) {
            sum += Math.abs(x);
        }
        double cost = dbs.binarySearch(0, sum);
        long ans = DigitUtils.round(solve(data, intervals, cost).maxValue + k * cost);
        return ans;
    }

    private static class LongSegmentQuery {
        int index;
        long val;

        public void reset() {
            index = -1;
            val = -LongSegment.inf;
        }

        public void update(int index, long val) {
            if (this.val < val) {
                this.index = index;
                this.val = val;
            }
        }
    }

    private static class DoubleSegmentQuery {
        int index;
        double val;

        public void reset() {
            index = -1;
            val = -DoubleSegment.inf;
        }

        public void update(int index, double val) {
            if (this.val < val) {
                this.index = index;
                this.val = val;
            }
        }
    }

    private static class LongSegment implements Cloneable {
        private static long inf = (long) 2e18;

        private LongSegment left;
        private LongSegment right;
        private long val = -inf;
        private int index = -1;

        public void pushUp() {
            val = Math.max(left.val, right.val);
            if (val == left.val) {
                index = left.index;
            } else {
                index = right.index;
            }
        }

        public void pushDown() {
        }

        public LongSegment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new LongSegment(l, m);
                right = new LongSegment(m + 1, r);
                pushUp();
            } else {
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, long x, int index) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                if (x > val) {
                    this.val = x;
                    this.index = index;
                }
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x, index);
            right.update(ll, rr, m + 1, r, x, index);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, LongSegmentQuery query) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                query.update(index, val);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, query);
            right.query(ll, rr, m + 1, r, query);
        }
    }

    private static class DoubleSegment implements Cloneable {
        private static double inf = 1e50;

        private DoubleSegment left;
        private DoubleSegment right;
        private double val = -inf;
        private int index = -1;

        public void pushUp() {
            val = Math.max(left.val, right.val);
            if(val == left.val){
                index = left.index;
            }else{
                index = right.index;
            }
        }

        public void pushDown() {
        }

        public DoubleSegment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new DoubleSegment(l, m);
                right = new DoubleSegment(m + 1, r);
                pushUp();
            } else {
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, double x, int index) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                if (x > val) {
                    this.val = x;
                    this.index = index;
                }
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x, index);
            right.update(ll, rr, m + 1, r, x, index);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, DoubleSegmentQuery query) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                query.update(index, val);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, query);
            right.query(ll, rr, m + 1, r, query);
        }
    }

}
