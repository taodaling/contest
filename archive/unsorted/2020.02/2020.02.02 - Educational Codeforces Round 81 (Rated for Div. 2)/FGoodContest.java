package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.IntegerList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FGoodContest {
    Modular mod = new Modular(998244353);
    InverseNumber inverseNumber = new InverseNumber(100, mod);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Interval[] intervals = new Interval[n];
        IntegerList list = new IntegerList(n * 2);
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval(in.readInt(), in.readInt());
            list.add(intervals[i].l);
            list.add(intervals[i].r);
        }
        SequenceUtils.reverse(intervals, 0, intervals.length - 1);

        list.unique();
        List<Interval> allPossibleIntervals = new ArrayList<>(list.size() * 2);
        allPossibleIntervals.add(new Interval(list.first(), list.first()));
        for (int i = 1; i < list.size(); i++) {
            int last = list.get(i - 1);
            int cur = list.get(i);
            if (cur - last > 1) {
                allPossibleIntervals.add(new Interval(last + 1, cur - 1));
            }
            allPossibleIntervals.add(new Interval(cur, cur));
        }

        Interval[] ranges = allPossibleIntervals.toArray(new Interval[0]);
        int m = ranges.length;
        int[][] dp = new int[m + 1][n + 1];
        dp[0][0] = 1;
        int[] comp = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                comp[j] = nonStrictlyIncrementSequence(ranges[i - 1].length(), j);
            }
            for (int j = 0; j <= n; j++) {
                dp[i][j] = mod.plus(dp[i][j], dp[i - 1][j]);
                for (int k = j - 1; k >= 0; k--) {
                    if (!intervals[k].contain(ranges[i - 1])) {
                        break;
                    }
                    int contrib = dp[i - 1][k];
                    contrib = mod.mul(comp[j - k], contrib);
                    dp[i][j] = mod.plus(dp[i][j], contrib);
                }
            }
        }

        // System.err.println(Arrays.deepToString(dp));
        int valid = dp[m][n];
        int total = 1;
        for (int i = 0; i < n; i++) {
            total = mod.mul(total, intervals[i].length());
        }

        int ans = valid;
        ans = mod.mul(ans, power.inverseByFermat(total));
        out.println(ans);
    }

    public int nonStrictlyIncrementSequence(int n, int k) {
        return composite(n - 1 + k, k);
    }

    /**
     * choose m elements in size n set
     */
    public int composite(int n, int m) {
        if (n < m) {
            return 0;
        }
        return m == 0 ? 1 : mod.mul(mod.mul(n, inverseNumber.inverse(m)), composite(n - 1, m - 1));
    }
}

class Interval {
    int l;
    int r;

    boolean contain(Interval other) {
        return l <= other.l && other.r <= r;
    }

    int length() {
        return r - l + 1;
    }

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", l, r);
    }
}
