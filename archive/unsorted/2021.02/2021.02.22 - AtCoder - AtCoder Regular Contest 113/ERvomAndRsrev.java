package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class ERvomAndRsrev {
    char[] s = new char[(int) 2e5];


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        StringBuilder ans = solve(n);
        out.println(ans);
    }

    public StringBuilder solve(int n) {
        List<Interval> intervals = new ArrayList<>(n);
        int zeroCnt = 0;
        int oneCnt = 0;
        for (int i = 0; i < n; i++) {
            int to = i;
            while (to + 1 < n && s[to + 1] == s[i]) {
                to++;
            }
            intervals.add(new Interval(s[i] - 'a', to - i + 1));
            i = to;
        }
        for (Interval interval : intervals) {
            if (interval.v == 0) {
                zeroCnt += interval.cnt;
            } else {
                oneCnt += interval.cnt;
            }
        }
        int m = intervals.size() - 1;
        if (intervals.get(m).v == 0) {
            int zero = intervals.get(m).cnt;
            int sum = 0;
            for (int i = 0; i < m; i++) {
                Interval interval = intervals.get(i);
                if (interval.v == 1) {
                    continue;
                }
                if (interval.cnt >= 2) {
                    sum += interval.cnt - 2;
                }
                sum = Math.max(sum, interval.cnt);
            }
            zero = CompareUtils.maxOf(zero, zero + sum - 2);
            if (zero % 2 != zeroCnt % 2) {
                zero--;
            }
            return output(0, oneCnt, zero, 0);
        }
        if (zeroCnt % 2 == 0) {
            return output(0, oneCnt, 0, 0);
        }
        if (intervals.get(0).v == 0 && intervals.get(0).cnt == zeroCnt) {
            if (oneCnt == 0) {
                return output(zeroCnt, oneCnt, 0, 0);
            } else {
                return output(zeroCnt % 2, oneCnt, 0, 0);
            }
        }
        //else
        if (intervals.get(m).cnt <= 2) {
            return output(0, oneCnt - intervals.get(m).cnt, zeroCnt % 2, intervals.get(m).cnt);
        }
        int[] dp = new int[2];
        if (intervals.get(0).v == 0) {
            dp[0] = intervals.get(0).cnt;
        }
        for (int i = 1; i <= m; i++) {
            Interval interval = intervals.get(i);
            if (interval.v == 1) {
                continue;
            }
            dp[1] = Math.max(dp[1], dp[1] + interval.cnt - 2);
            dp[1] = Math.max(dp[1], dp[0] + interval.cnt - 2);
            dp[1] = Math.max(dp[1], interval.cnt);
        }
        return output(0, oneCnt - 2, maxOdd(dp[1]), 0);
    }

    public int maxOdd(int x) {
        if (x <= 0) {
            return x;
        }
        if (x % 2 == 0) {
            x--;
        }
        return x;
    }

    public StringBuilder output(int a, int b, int c, int d) {
        StringBuilder ans = new StringBuilder(a + b + c + d);
        for (int i = 0; i < a; i++) {
            ans.append('a');
        }
        for (int i = 0; i < b; i++) {
            ans.append('b');
        }
        for (int i = 0; i < c; i++) {
            ans.append('a');
        }
        for (int i = 0; i < d; i++) {
            ans.append('b');
        }
        return ans;
    }
}

class Interval {
    int v;
    int cnt;

    public Interval(int v, int cnt) {
        this.v = v;
        this.cnt = cnt;
    }
}
