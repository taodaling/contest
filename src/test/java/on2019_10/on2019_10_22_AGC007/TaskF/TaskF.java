package on2019_10.on2019_10_22_AGC007.TaskF;



import java.util.ArrayList;
import java.util.List;

import template.SequenceUtils;
import template.FastInput;
import template.FastOutput;
import template.Randomized;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n + 1];
        char[] t = new char[n + 1];
        in.readString(s, 1);
        in.readString(t, 1);

        List<Interval> intervalList = new ArrayList<>(n);
        int scan = n + 1;
        for (int i = n; i >= 1; i--) {
            if (scan > i) {
                scan = i;
            }
            if (s[scan] == t[i]) {
                continue;
            }
            while (s[scan] != t[i] && scan > 1) {
                scan--;
            }
            if (s[scan] != t[i]) {
                out.println(-1);
                return;
            }
            Interval interval = new Interval();
            interval.l = scan;
            interval.r = i;
            intervalList.add(interval);
        }

        Interval[] intervals = intervalList.toArray(new Interval[0]);
        SequenceUtils.reverse(intervals, 0, intervals.length);

        int m = intervals.length;
        int[] perm = new int[m];
        for (int i = 0; i < m; i++) {
            perm[i] = i;
        }
        Randomized.randomizedArray(perm, 0, m);

        int ans = 0;
        for (int i = 0; i < m; i++) {
            int j = perm[i];
            if (j + ans >= m || intervals[j].r <= intervals[j + ans].l - (ans)) {
                continue;
            }
            int l = ans + 1;
            int r = m - j;
            while (l < r) {
                int mid = (l + r) >> 1;
                if (j + mid >= m || intervals[j].r <= intervals[j + mid].l - mid) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            ans = l;
        }

        out.println(ans);
    }
}


class Interval {
    int l;
    int r;
}
