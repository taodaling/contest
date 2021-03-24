package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SortUtils;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class FSouvenirs {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        int m = in.readInt();
        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
        }
        Query[] sorted = qs.clone();
        Arrays.sort(sorted, (x, y) -> Integer.compare(x.r, y.r));
        solve(a, sorted);
        int max = (int) 1e9;
        for (int i = 0; i < n; i++) {
            a[i] = max - a[i];
        }
        solve(a, sorted);

        for(int i = 0; i < m; i++){
            out.println(qs[i].ans);
        }
    }

    public void solve(int[] a, Query[] qs) {
        int n = a.length;
        IntegerArrayList[] ceil = new IntegerArrayList[n];
        IntegerArrayList buf = new IntegerArrayList(60);
        for (int i = 0; i < n; i++) {
            ceil[i] = new IntegerArrayList();
        }
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> a[i] == a[j] ? Integer.compare(i, j) : -Integer.compare(a[i], a[j]), 0, n);

        int inf = (int) 2e9;
        SegmentBS segBS = new SegmentBS(0, n - 1);
        segBS.reset(0, n - 1, inf);
        for (int i : indices) {
            int threshold = inf - 1;
            int j = segBS.query(0, i, 0, n - 1, threshold);
            buf.clear();
            while (j != -1) {
                buf.add(j);
                //x - a[i] < a[j] - x
                //2x < a[j] + a[i]
                threshold = (int) DigitUtils.maximumIntegerLessThanDiv(a[j] + a[i], 2);
                j = segBS.query(0, j - 1, 0, n - 1, threshold);
            }
            ceil[i].addAll(buf);
            segBS.update(i, i, 0, n - 1, a[i]);
        }

        Segment segtree = new Segment(0, n - 1);
        int l = 0;
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < ceil[i].size(); t++) {
                int j = ceil[i].get(t);
                segtree.update(0, j, 0, n - 1, a[j] - a[i]);
            }
            while (l < qs.length && qs[l].r == i) {
                qs[l].ans = Math.min(qs[l].ans, segtree.queryL(qs[l].l, qs[l].l, 0, n - 1));
                l++;
            }
        }
    }
}

class Query {
    int l;
    int r;
    int ans = (int) 2e9;
}

class SegmentBS implements Cloneable {
    private SegmentBS left;
    private SegmentBS right;
    private int min;

    private void modify(int x) {
        min = x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
    }

    public SegmentBS(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new SegmentBS(l, m);
            right = new SegmentBS(m + 1, r);
            pushUp();
        } else {

        }
    }

    public void reset(int l, int r, int x) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.reset(l, m, x);
            right.reset(m + 1, r, x);
            pushUp();
        } else {
            min = x;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
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

    public int query(int ll, int rr, int l, int r, int threshold) {
        if (noIntersection(ll, rr, l, r) || min > threshold) {
            return -1;
        }
        if (covered(ll, rr, l, r) && l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        int ans = right.query(ll, rr, m + 1, r, threshold);
        if (ans == -1) {
            ans = left.query(ll, rr, l, m, threshold);
        }
        return ans;
    }

    private SegmentBS deepClone() {
        SegmentBS seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected SegmentBS clone() {
        try {
            return (SegmentBS) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(min).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int min;
    private static int inf = (int) 2e9;

    private void modify(int x) {
        min = Math.min(min, x);
    }

    public void pushUp() {
    }

    public void pushDown() {
        left.modify(min);
        right.modify(min);
        min = inf;
    }

    public Segment(int l, int r) {
        min = inf;
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
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

    public void update(int ll, int rr, int l, int r, int x) {
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

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(min).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
