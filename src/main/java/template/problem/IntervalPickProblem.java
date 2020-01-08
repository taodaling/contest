package template.problem;

import template.primitve.generated.LongPreSum;
import template.primitve.generated.MinIntegerQueue;

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

    public static long solve(long[] data, Interval[] intervals) {
        LongPreSum lps = new LongPreSum(data);
        int n = intervals.length;
        long[] dp = new long[n];
        int[] last = new int[n];

        MinIntegerQueue left = new MinIntegerQueue(n, (a, b) -> -Long.compare(dp[a], dp[b]));
        MinIntegerQueue middle = new MinIntegerQueue(n, (a, b) -> -Long.compare(dp[a] - lps.prefix(intervals[a - 1].r), dp[b] - lps.prefix(intervals[b - 1].r)));
        left.addLast(0);
        for (int i = 1; i <= n; i++) {
            Interval now = intervals[i - 1];
            while (!middle.isEmpty() && intervals[middle.peek() - 1].r < now.l) {
                left.addLast(middle.removeFirst());
            }
            dp[i] = Long.MIN_VALUE;
            if (dp[i] < dp[left.min()] + lps.intervalSum(now.l, now.r)) {
                dp[i] = dp[left.min()] + lps.intervalSum(now.l, now.r);
                last[i] = left.min();
            }
            if (!middle.isEmpty() && dp[i] < dp[middle.min()] + lps.prefix(now.r) - lps.prefix(intervals[middle.min() - 1].r)) {
                dp[i] = dp[middle.min()] + lps.prefix(now.r) - lps.prefix(intervals[middle.min() - 1].r);
                last[i] = middle.min();
            }
            middle.addLast(i);
        }

        int maxIndex = 0;
        for (int i = 1; i < n; i++) {
            if (dp[i] > dp[maxIndex]) {
                maxIndex = i;
            }
        }

        int trace = maxIndex;
        while (trace > 0) {
            intervals[trace - 1].used = true;
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
        int n = intervals.length;
        double[] dp = new double[n];
        int[] time = new int[n];

        MinIntegerQueue left = new MinIntegerQueue(n, (a, b) -> -Double.compare(dp[a], dp[b]));
        MinIntegerQueue middle = new MinIntegerQueue(n, (a, b) -> -Double.compare(dp[a] - lps.prefix(intervals[a - 1].r), dp[b] - lps.prefix(intervals[b - 1].r)));
        left.addLast(0);
        for (int i = 1; i <= n; i++) {
            Interval now = intervals[i - 1];
            while (!middle.isEmpty() && intervals[middle.peek() - 1].r < now.l) {
                left.addLast(middle.removeFirst());
            }
            dp[i] = Long.MIN_VALUE;
            if (dp[i] < dp[left.min()] + lps.intervalSum(now.l, now.r)) {
                dp[i] = dp[left.min()] + lps.intervalSum(now.l, now.r);
                time[i] = time[left.min()] + 1;
            }
            if (!middle.isEmpty() && dp[i] < dp[middle.min()] + lps.prefix(now.r) - lps.prefix(intervals[middle.min() - 1].r)) {
                dp[i] = dp[middle.min()] + lps.prefix(now.r) - lps.prefix(intervals[middle.min() - 1].r);
                time[i] = time[middle.min()] + 1;
            }
            dp[i] -= cost;
            middle.addLast(i);
        }

        int maxIndex = 0;
        for (int i = 1; i < n; i++) {
            if (dp[i] > dp[maxIndex]) {
                maxIndex = i;
            }
        }

        int trace = maxIndex;
        while (trace > 0) {
            intervals[trace - 1].used = true;
            trace = time[trace];
        }

        return new WQSResult(dp[maxIndex], time[maxIndex]);
    }

    /**
     * all elements in data should be non-negative.
     */
    public static long solve(long[] data, Interval[] intervals, int k) {

    }
}
