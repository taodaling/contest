package on2019_10.on2019_10_24_Educational_Codeforces_Round_75__Rated_for_Div__2_.D___Salary_Changing;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskD {
    int n;
    long s;
    int[][] lrs;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        s = in.readLong();
        lrs = new int[n][2];
        for (int i = 0; i < n; i++) {
            lrs[i][0] = in.readInt();
            lrs[i][1] = in.readInt();
        }
        int l = 1;
        int r = (int) 1e9;

        while (l < r) {
            int m = (l + r + 1) >> 1;
            if (check(m)) {
                l = m;
            } else {
                r = m - 1;
            }
        }

        out.println(l);
    }

    PriorityQueue<int[]> qs1 = new PriorityQueue<>(200000, (a, b) -> -(a[0] - b[0]));
    PriorityQueue<int[]> qs2 = new PriorityQueue<>(200000, (a, b) -> a[0] - b[0]);

    public boolean check(int m) {
        qs1.clear();
        qs2.clear();
        for (int i = 0; i < n; i++) {
            if (lrs[i][1] >= m) {
                qs1.add(lrs[i]);
            } else {
                qs2.add(lrs[i]);
            }
        }

        int half = DigitUtils.ceilDiv(n, 2);
        if (qs1.size() < half) {
            return false;
        }

        long total = 0;
        for (int i = 0; i < half; i++) {
            total += Math.max(m, qs1.remove()[0]);
        }

        while (!qs1.isEmpty()) {
            total += qs1.remove()[0];
        }

        while (!qs2.isEmpty()) {
            total += qs2.remove()[0];
        }

        return total <= s;
    }
}
