package on2020_10.on2020_10_26_Library_Checker.Rectangle_Sum;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class RectangleSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        Point[] pts = new Point[n];
        Query[] qs = new Query[q];
        TreeMap<Integer, NoTagPersistentSegment> map = new TreeMap<>();
        map.put(Integer.MIN_VALUE, NoTagPersistentSegment.NIL);
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(in.readInt(), in.readInt(), in.readInt());
        }
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt();
            qs[i].b = in.readInt();
            qs[i].r = in.readInt() - 1;
            qs[i].t = in.readInt() - 1;
        }
        int rangeL = 0;
        int rangeR = (int) 1e9;
        Arrays.sort(pts, (a, b) -> Integer.compare(a.x, b.x));
        Query[] sortedQs = qs.clone();
        Arrays.sort(sortedQs, (a, b) -> Integer.compare(a.r, b.r));
        SimplifiedDeque<Point> ptDq = new Range2DequeAdapter<>(i -> pts[i], 0, n - 1);
        SimplifiedDeque<Query> qsDq = new Range2DequeAdapter<>(i -> sortedQs[i], 0, q - 1);
        while (!qsDq.isEmpty()) {
            if (!ptDq.isEmpty() && ptDq.peekFirst().x <= qsDq.peekFirst().r) {
                Point pt = ptDq.removeFirst();
                NoTagPersistentSegment seg = map.floorEntry(pt.x).getValue().clone();
                seg.update(pt.y, pt.y, rangeL, rangeR, pt.w);
                map.put(pt.x, seg);
            } else {
                Query query = qsDq.removeFirst();
                NoTagPersistentSegment plus = map.floorEntry(query.r).getValue();
                NoTagPersistentSegment sub = map.floorEntry(query.l - 1).getValue();
                long ans = plus.query(query.b, query.t, rangeL, rangeR) -
                        sub.query(query.b, query.t, rangeL, rangeR);
                query.ans = ans;
            }
        }

        for (Query query : qs) {
            out.println(query.ans);
        }
    }
}

class Point {
    int x;
    int y;
    int w;

    public Point(int x, int y, int w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }
}

class Query {
    int l;
    int r;
    int b;
    int t;
    long ans;
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
    private long sum;

    public void pushUp() {
        sum = left.sum + right.sum;
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

    public void update(int ll, int rr, int l, int r, long x) {
        if (covered(ll, rr, l, r)) {
            sum += x;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, x);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, x);
        }
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
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
