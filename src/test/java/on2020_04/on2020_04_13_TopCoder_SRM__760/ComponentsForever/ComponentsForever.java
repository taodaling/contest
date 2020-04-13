package on2020_04.on2020_04_13_TopCoder_SRM__760.ComponentsForever;



import template.datastructure.DiscreteMap;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongDiscreteMap;
import template.primitve.generated.datastructure.LongList;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentsForever {
    Debug debug = new Debug(true);
    public int countComponents(int n, int[] Xprefix, int[] Yprefix, int seed, int Xrange, int Yrange, int Llo, int Rhi) {
        Point[] points = new Point[n];

        for (int i = 0; i < Xprefix.length; i++) {
            points[i] = new Point(Xprefix[i], Yprefix[i]);
        }
        long state = seed;
        for (int i = Xprefix.length; i < n; i++) {
            state = (1103515245 * state + 12345);
            long x = state % Xrange;
            state = state % (1L << 31);
            state = (1103515245 * state + 12345);
            long y = state % Yrange;
            state = state % (1L << 31);

            points[i] = new Point(x, y);
        }

      //  debug.debug("points", points);

        List<Point>[] classify = new List[2];
        for (int i = 0; i < 2; i++) {
            classify[i] = new ArrayList<>(n);
        }

        for (int i = 0; i < n; i++) {
            if (points[i].xx % 2 == points[i].yy % 2) {
                classify[0].add(points[i]);
            } else {
                classify[1].add(points[i]);
            }
        }

        int ans = 0;
        for(int i = 0; i < 2; i++){
            int exp = exp(classify[i].toArray(new Point[0]), Llo, Rhi);
           // debug.debug("exp", exp);
            ans = mod.plus(ans, exp);
        }
        return ans;
    }

    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);

    public int pick2(int n) {
        return mod.valueOf((long) n * (n - 1) / 2);
    }

    public int pick2(int l, int r) {
        if(l > r){
            return 0;
        }
        return pick2(r - l + 1);
    }

    public int exp(Point[] points, int l, int r) {
        LongList ys = new LongList(points.length);
        for (Point pt : points) {
            long x = pt.xx;
            long y = pt.yy;
            pt.x = x + y;
            pt.y = x - y;
            ys.add(pt.y);
        }
        LongDiscreteMap dm = new LongDiscreteMap(ys.getData(), 0, ys.size());
        Arrays.sort(points, (a, b) -> a.x == b.x ? Long.compare(a.y, b.y) : Long.compare(a.x, b.x));
        Segment top = new Segment(dm.minRank(), dm.maxRank());
        Segment bot = new Segment(dm.minRank(), dm.maxRank());

        int total = pick2(l, r);
        int ans = 0;
        for (Point pt : points) {
            int yR = dm.rankOf(pt.y);
            long topC = top.query(yR, dm.maxRank(), dm.minRank(), dm.maxRank()) - pt.y + pt.x;
            long botC = bot.query(0, yR, dm.minRank(), dm.maxRank()) + pt.y + pt.x;
            long atLeast = DigitUtils.ceilDiv(Math.min(topC, botC), 2);
            debug.debug("atLeast", atLeast + " for " + pt);
            if (atLeast > r) {
                ans = mod.plus(ans, total);
            } else if (atLeast <= l) {
            } else {
                ans = mod.plus(ans, pick2(l, (int) atLeast - 1));
            }

            top.update(yR, yR, dm.minRank(), dm.maxRank(), (long)-pt.x + pt.y);
            bot.update(yR, yR, dm.minRank(), dm.maxRank(), (long)-pt.x - pt.y);
        }

        int exp = mod.mul(ans, power.inverseByFermat(total));
        return exp;
    }

}

class Point {
    long x;
    long y;
    long xx;
    long yy;

    public Point(long xx, long yy) {
        this.xx = xx;
        this.yy = yy;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")|(" + xx + "," + yy + ")";
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long inf = (long) 1e18;
    private long val = inf;

    public void modify(long x) {
        this.val = Math.min(val, x);
    }

    public void pushUp() {
        val = Math.min(left.val, right.val);
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

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
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
            builder.append(val).append(",");
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
