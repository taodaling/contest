package on2021_09.on2021_09_08_DMOJ___DMOPC__21_Contest_1.P5___Perfect_Cross;



import template.algo.BinarySearch;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.FractionComparator;
import template.utils.CloneSupportObject;
import template.utils.UpdatableSum;
import template.utils.Update;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;

public class P5PerfectCross {
    int s;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        s = in.ri();
        Point[] pts = new Point[n];
        Point[] flipedPts = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            int y = in.ri();
            pts[i] = new Point(x + y, x - y, i);
            flipedPts[i] = pts[i].clone();
            flipedPts[i].swap();
        }
        SubQuery[] hs = new SubQuery[q];
        SubQuery[] vs = new SubQuery[q];
        for (int i = 0; i < q; i++) {
            int x = in.ri();
            int y = in.ri();
            long cx = x + y;
            long cy = x - y;
            hs[i] = new SubQuery();
            hs[i].bot = cy - s;
            hs[i].top = cy + s;
            hs[i].x = cx + s;

            vs[i] = new SubQuery();
            vs[i].bot = cx - s;
            vs[i].top = cx + s;
            vs[i].x = cy + s;
        }
        solve(pts.clone(), hs.clone());
        solve(flipedPts.clone(), vs.clone());
        for (int i = 0; i < q; i++) {
            out.append(hs[i].result.a.id + 1).append(' ').append(hs[i].result.b.id + 1).append(' ');
            out.append(vs[i].result.a.id + 1).append(' ').append(vs[i].result.b.id + 1).println();
        }
    }

    public void solve(Point[] pts, SubQuery[] qs) {
        int n = pts.length;
        Arrays.sort(pts, Comparator.comparingLong(x -> x.y));
        for (int i = 0; i < n; i++) {
            pts[i].offset = i;
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new,
                UpdateImpl::new, i -> {
            SumImpl s = new SumImpl();
            s.pt = pts[i];
            return s;
        });
        Point[] sortByY = pts.clone();
        Arrays.sort(pts, Comparator.comparingLong(x -> x.x));
        int lIter = 0;
        int rIter = 0;
        Arrays.sort(qs, Comparator.comparingLong(x -> x.x));
        UpdateImpl upd = new UpdateImpl();
        for (SubQuery q : qs) {
            long l = q.x - 2L * s;
            long r = q.x;
            while (rIter < n && pts[rIter].x <= r) {
                Point head = pts[rIter++];
                upd.mod = 1;
                st.update(head.offset, head.offset, 0, n - 1, upd);
            }
            while (lIter < n && pts[lIter].x < l) {
                Point head = pts[lIter++];
                upd.mod = -1;
                st.update(head.offset, head.offset, 0, n - 1, upd);
            }
            int begin = BinarySearch.firstTrue(new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return sortByY[value].y >= q.bot;
                }
            }, 0, n - 1);
            int end = BinarySearch.lastTrue(new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return sortByY[value].y <= q.top;
                }
            }, 0, n - 1);
            SumImpl sum = new SumImpl();
            st.query(begin, end, 0, n - 1, sum);
            q.result = sum.br;
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int mod;

    @Override
    public void update(UpdateImpl update) {
        mod += update.mod;
    }

    @Override
    public void clear() {
        mod = 0;
    }

    @Override
    public boolean ofBoolean() {
        return mod != 0;
    }
}

class SumImpl implements UpdatableSum<SumImpl, UpdateImpl> {
    Point left;
    Point right;
    Point pt;
    BestResult br = new BestResult();
    int occur;

    @Override
    public void add(SumImpl right) {
        br.update(right.br);
        br.update(this.right, right.left, Point.line(this.right, right.left));
        if (left == null) {
            left = right.left;
        }
        if (right.right != null) {
            this.right = right.right;
        }
        occur += right.occur;
    }

    @Override
    public void copy(SumImpl right) {
        left = right.left;
        this.right = right.right;
        br.copy(right.br);
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public void update(UpdateImpl update) {
        occur += update.mod;
        if (update.mod > 0) {
            left = right = pt;
            br.init();
        } else {
            left = right = null;
            br.init();
        }
    }
}


class BestResult extends CloneSupportObject<BestResult> {
    Point a;
    Point b;
    double best = 1e100;

    public void copy(BestResult res) {
        a = res.a;
        b = res.b;
        best = res.best;
    }

    public void update(Point x, Point y, double fr) {
        if (best > fr) {
            a = x;
            b = y;
            best = fr;
        }
    }

    public void init() {
        a = b = null;
        best = 1e100;
    }

    public void update(BestResult br) {
        update(br.a, br.b, br.best);
    }
}

class SubQuery {
    long x;
    long bot;
    long top;
    BestResult result;
}

class Point extends CloneSupportObject<Point> {
    long x;
    long y;
    int id;
    int offset;

    public Point(long x, long y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void swap() {
        long tmp = x;
        x = y;
        y = tmp;
    }

    static double line(Point a, Point b) {
        if (a == null || b == null) {
            return 1e100;
        }
        if(a.x == b.x){
            return 1e50;
        }
        return Math.abs(a.y - b.y) / (double)Math.abs(a.x - b.x);
    }
}
