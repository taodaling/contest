package contest;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskC {

    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
    NumberTheory.Composite comp = new NumberTheory.Composite(10000, mod);

    int[] preSum;
    int[][] dp;
    int n;
    int m;
    Interval[] intervals;
    int zero = 3000;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        char[] s = (" " + in.readString()).toCharArray();
        preSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            preSum[i] = preSum[i - 1] + s[i] - '0';
        }

        intervals = new Interval[m];
        for (int i = 0; i < m; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt();
            intervals[i].r = in.readInt();
        }
        Arrays.sort(intervals, (a, b) -> a.l == b.l ? -(a.r - b.r) : a.l - b.l);
        Deque<Interval> deque = new ArrayDeque<>(m);
        for (int i = 0; i < m; i++) {
            if (deque.isEmpty()) {
                deque.addLast(intervals[i]);
            } else if (deque.peekLast().l < intervals[i].l && deque.peekLast().r < intervals[i].r) {
                deque.addLast(intervals[i]);
            }
        }
        intervals = deque.toArray(new Interval[0]);
        m = intervals.length;

        dp = new int[m + 1][6001];
        ArrayUtils.deepFill(dp, -1);
        Arrays.fill(dp[m], 0);
        dp[m][zero] = 1;

        int ans = dp(0, 3000);
        out.println(ans);
    }

    public int dp(int i, int j) {
        if (dp[i][j] == -1) {
            dp[i][j] = 0;
            Interval interval = intervals[i];
            int len = interval.r - interval.l + 1;
            int oneCnt = j - zero + preSum[interval.r] - preSum[interval.l - 1];
            int zeroCnt = len - oneCnt;

            int commonLength = 0;
            int originOneCnt = 0;
            if (i < m - 1 && intervals[i + 1].l <= interval.r) {
                commonLength = interval.r - intervals[i + 1].l + 1;
                originOneCnt = preSum[interval.r] - preSum[intervals[i + 1].l - 1];
            }
            int nonCommonLength = len - commonLength;

            for (int k = Math.max(commonLength - zeroCnt, 0); k <= oneCnt && k <= commonLength; k++) {
                int contri = mod.mul(dp(i + 1, zero - originOneCnt + k), comp.composite(nonCommonLength, oneCnt - k));
                dp[i][j] = mod.plus(dp[i][j], contri);
            }
        }
        return dp[i][j];
    }
}


class Interval {
    int l;
    int r;
}
