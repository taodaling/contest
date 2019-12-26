package on2019_12.on2019_12_26_Codeforces_Round__524__Div__2_.F__Katya_and_Segments_Sets;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FKatyaAndSegmentsSets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        Interval[] intervals = new Interval[k];
        for (int i = 0; i < k; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt();
            intervals[i].r = in.readInt();
            intervals[i].p = in.readInt();
        }

        Arrays.sort(intervals, (a, b) -> a.l - b.l);
        Interval[] next = new Interval[n + 1];
        for (int i = intervals.length - 1; i >= 0; i--) {
            Interval interval = intervals[i];
            if (next[interval.p] != null) {
                interval.r = Math.min(interval.r, next[interval.p].r);
            }
            next[interval.p] = interval;
        }

        NoTagPersistentSegment[] ps = new NoTagPersistentSegment[k];
        NoTagPersistentSegment last = NoTagPersistentSegment.NIL;
        for (int i = 0; i < k; i++) {
            Interval interval = intervals[i];
            ps[i] = last.clone();
            ps[i].update(interval.p, interval.p, 1, n, interval.l, interval.r);
            last = ps[i];
        }

        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int x = in.readInt();
            int y = in.readInt();
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    return ps[mid].queryL(a, b, 1, n) >= x;
                }
            };
            int index = ibs.binarySearch(0, k - 1);
            if (!ibs.check(index) || ps[index].queryR(a, b, 1, n) > y) {
                out.println("no");
            } else {
                out.println("yes");
            }
            out.flush();
        }
    }
}

class Interval {
    int l;
    int r;
    int p;
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();
    private static final int INF = (int) 2e9;

    static {
        NIL.left = NIL.right = NIL;
        NIL.r = INF;
        NIL.l = -INF;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
    private int r;
    private int l;

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        r = Math.max(left.r, right.r);
        l = Math.min(left.l, right.l);
    }

    public NoTagPersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int xl, int xr) {
        if (covered(ll, rr, l, r)) {
            this.r = xr;
            this.l = xl;
            return;
        }
        int m = (l + r) >> 1;
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, xl, xr);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, xl, xr);
        }
        pushUp();
    }

    public int queryL(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return INF;
        }
        if (this == NIL || covered(ll, rr, l, r)) {
            return this.l;
        }
        int m = (l + r) >> 1;
        return Math.min(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
    }

    public int queryR(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -INF;
        }
        if (this == NIL || covered(ll, rr, l, r)) {
            return this.r;
        }
        int m = (l + r) >> 1;
        return Math.max(left.queryR(ll, rr, l, m),
                right.queryR(ll, rr, m + 1, r));
    }

    @Override
    public NoTagPersistentSegment clone() {
        try {
            return (NoTagPersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

