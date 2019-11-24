package on2019_11.on2019_11_24_AtCoder_Beginner_Contest_146.F___Sugoroku;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[] s = new char[n + 1];
        in.readString(s, 0);
        int[] dp = new int[n + 1];
        int[] req = new int[n + 1];
        Arrays.fill(req, (int) 1e8);
        Segment seg = new Segment(0, n);
        seg.update(n, n, 0, n, 0);
        req[n] = 0;
        int tail = n;
        for (int i = n - 1; i >= 0; i--) {
            while (tail > i + m) {
                seg.update(tail, tail, 0, n, (int) 1e8);
                req[tail] = (int) 1e8;
                tail--;
            }
            if (s[i] == '1') {
                continue;
            }
            int index = seg.query(0, n);
            dp[i] = index;
            req[i] = req[index] + 1;
            seg.update(i, i, 0, n, req[i]);
        }
        if (req[0] > n) {
            out.println(-1);
            return;
        }
        int last = 0;
        while (last != n) {
            out.append(dp[last] - last).append(' ');
            last = dp[last];
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int minStep = (int) 1e8;

    public void pushUp() {
        minStep = Math.min(left.minStep, right.minStep);
    }

    public void modify(int minStep) {
        this.minStep = minStep;
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
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

    public void update(int ll, int rr, int l, int r, int minStep) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(minStep);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, minStep);
        right.update(ll, rr, m + 1, r, minStep);
        pushUp();
    }

    public int query(int l, int r) {
        Segment trace = this;
        while (l < r) {
            pushDown();
            int m = (l + r) >> 1;
            if (trace.left.minStep <= trace.right.minStep) {
                r = m;
                trace = trace.left;
            } else {
                l = m + 1;
                trace = trace.right;
            }
        }
        return l;
    }
}
