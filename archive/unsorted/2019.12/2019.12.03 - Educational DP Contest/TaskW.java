package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskW {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Interval[] intervals = new Interval[m];
        for (int i = 0; i < m; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt();
            intervals[i].r = in.readInt();
            intervals[i].a = in.readInt();
        }
        Arrays.sort(intervals, (a, b) -> a.r - b.r);
        Deque<Interval> deque = new ArrayDeque<>(Arrays.asList(intervals));
        Segment seg = new Segment(0, n);
        seg.update(0, 0, 0, n, -seg.queryMax(0, 0, 0, n));
        for (int i = 1; i <= n; i++) {
            long max = seg.queryMax(0, i - 1, 0, n);
            seg.update(i, i, 0, n, max - seg.queryMax(i, i, 0, n));
            while (!deque.isEmpty() && deque.peekFirst().r == i) {
                Interval head = deque.removeFirst();
                seg.update(head.l, head.r, 0, n, head.a);
            }
        }
        long ans = seg.queryMax(0, n, 0, n);
        out.println(ans);
    }
}

class Interval {
    int l;
    int r;
    int a;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long val;
    private long max;

    public void modify(long x) {
        max += x;
        val += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        if (val != 0) {
            left.modify(val);
            right.modify(val);
            val = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            val = max = (long) -1e18;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (long) -1e18;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }
}
