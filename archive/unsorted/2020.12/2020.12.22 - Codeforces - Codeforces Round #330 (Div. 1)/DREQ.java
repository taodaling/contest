package contest;

import template.datastructure.Range2DequeAdapter;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DREQ {
    int mod = (int) 1e9 + 7;
    InverseNumber inv = new ModPrimeInverseNumber((int) 1e6, mod);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve((int) 1e6);
        Segment prodSeg = new Segment(0, n - 1, i -> a[i]);

        List<Point> pts = new ArrayList<>(n * 20);
        int[] prev = new int[(int) 1e6 + 1];
        int[] factors = sieve.getSmallestPrimeFactor();
        Arrays.fill(prev, -1);
        for (int i = 0; i < n; i++) {
            int x = a[i];
            while (x > 1) {
                int factor = factors[x];
                while (x % factor == 0) {
                    x /= factor;
                }
                int val = DigitUtils.modsub(1, inv.inverse(factor), mod);
                pts.add(new Point(i, prev[factor], val));
                prev[factor] = i;
            }
        }
        Segment moveSeg = new Segment(-1, n, i -> 1);
        int q = in.ri();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query(in.ri() - 1, in.ri() - 1);
        }
        Query[] sortedByR = qs.clone();
        Query[] sortedByL = qs.clone();
        Arrays.sort(sortedByR, Comparator.comparingInt(x -> x.r));
        Arrays.sort(sortedByL, Comparator.comparingLong(x -> x.l));
        Range2DequeAdapter<Query> dql = new Range2DequeAdapter<>(i -> sortedByL[i], 0, q - 1);
        Range2DequeAdapter<Query> dqr = new Range2DequeAdapter<>(i -> sortedByR[i], 0, q - 1);
        Range2DequeAdapter<Point> ptDq = new Range2DequeAdapter<>(i -> pts.get(i), 0, pts.size() - 1);
        while (!dqr.isEmpty()) {
            int time = dqr.peekFirst().r;
            if (!dql.isEmpty()) {
                time = Math.min(dql.peekFirst().l - 1, time);
            }
            while (!ptDq.isEmpty() && ptDq.peekFirst().x <= time) {
                Point head = ptDq.removeFirst();
                moveSeg.update(head.y, head.y, -1, n, head.val);
            }
            while (!dql.isEmpty() && dql.peekFirst().l - 1 == time) {
                Query head = dql.removeFirst();
                head.inv = (int) moveSeg.query(-1, head.l - 1, -1, n);
            }
            while (!dqr.isEmpty() && dqr.peekFirst().r == time) {
                Query head = dqr.removeFirst();
                head.ans = (int) moveSeg.query(-1, head.l - 1, -1, n);
            }
        }
        for (Query query : qs) {
            long ans = (long)query.ans * power.inverse(query.inv) % mod *
                    prodSeg.query(query.l, query.r, 0, n - 1) % mod;
            out.println(ans);
        }
    }
}

class Query {
    int l;
    int r;
    int ans;
    int inv;

    public Query(int l, int r) {
        this.l = l;
        this.r = r;
    }
}

class Point {
    int x;
    int y;
    int val;

    public Point(int x, int y, int val) {
        this.x = x;
        this.y = y;
        this.val = val;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static int mod = (int) 1e9 + 7;
    long prod = 1;

    private void modify(long x) {
        prod = prod * x % mod;
    }

    public void pushUp() {
        prod = left.prod * right.prod % mod;
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            prod = func.apply(l);
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
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
        if (leave(ll, rr, l, r)) {
            return 1;
        }
        if (enter(ll, rr, l, r)) {
            return prod;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) *
                right.query(ll, rr, m + 1, r) % mod;
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
            builder.append("val").append(",");
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
