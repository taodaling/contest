package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        Problem[] ps = new Problem[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new Problem();
            ps[i].d = in.readInt();
            ps[i].p = a - in.readInt();
        }
        long ans = dac(ps, 0, n - 1);
        out.println(ans);
    }

    public long dac(Problem[] ps, int l, int r) {
        if (l == r) {
            return Math.max(0, ps[l].p);
        }
        int m = (l + r) >>> 1;
        long ans = Math.max(dac(ps, l, m), dac(ps, m + 1, r));

        long cost = pow2(ps[m].d - ps[m + 1].d);
        long lBest = 0;
        long rBest = 0;
        long lSum = 0;
        long rSum = 0;
        int i = m;
        int j = m + 1;
        while (i >= l || j <= r) {
            if (i >= l && (j > r || pow2(ps[i].d - ps[i + 1].d)
                    <= pow2(ps[j].d - ps[j - 1].d))) {
                lSum += ps[i].p;
                cost = Math.max(cost, pow2(ps[i].d - ps[i + 1].d));
                lBest = Math.max(lBest, lSum);
                i--;
            } else {
                rSum += ps[j].p;
                cost = Math.max(cost, pow2(ps[j].d - ps[j - 1].d));
                rBest = Math.max(rBest, rSum);
                j++;
            }
            ans = Math.max(ans, lBest + rBest - cost);
        }
        return ans;
    }

    long pow2(long x) {
        return x * x;
    }
}

class Problem {
    int d;
    int p;
}
