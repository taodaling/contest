package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CUltimateWeirdnessOfAnArray {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int limit = (int) 2e5;
        int[] idx = new int[limit + 1];
        Arrays.fill(idx, -1);
        for (int i = 0; i < n; i++) {
            idx[a[i]] = i;
        }

        List<Point> pts = new ArrayList<>(limit * 20);
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = limit; i >= 1; i--) {
            int max = -1;
            int min = n;
            list.clear();
            for (int j = i; j <= limit; j += i) {
                if (idx[j] == -1) {
                    continue;
                }
                list.add(idx[j]);
            }
            list.sort();
            if (list.size() <= 1) {
                continue;
            }
            pts.add(new Point(list.first() + 1, list.tail() - 1, i));
            pts.add(new Point(0, list.get(list.size() - 2) - 1, i));
            pts.add(new Point(list.get(1) + 1, n - 1, i));
        }
        pts.sort((x, y) -> Integer.compare(x.l, y.l));
        int l = 0;
        SegmentBeat sb = new SegmentBeat(0, n - 1, i -> 0);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            while (l < pts.size() && pts.get(l).l == i) {
                Point first = pts.get(l++);
                sb.updateMin(0, first.r, 0, n - 1, -first.gcd);
            }
            long contrib = sb.querySum(i, n - 1, 0, n - 1);
            sum += contrib;
            debug.debug("contrib", -contrib);
        }
        out.println(-sum);
    }
}

class Point {
    int l;
    int r;
    int gcd;

    public Point(int l, int r, int gcd) {
        this.l = l;
        this.r = r;
        this.gcd = gcd;
    }

    @Override
    public String toString() {
        return "[" + l + ", " + r + "]=" + gcd;
    }
}


class SegmentBeat implements Cloneable {
    private SegmentBeat left;
    private SegmentBeat right;
    private long first;
    private long second;
    private int firstCnt;
    private static long inf = (long) 2e18;
    private long sum;

    private void setMin(long x) {
        if (first <= x) {
            return;
        }
        sum -= (first - x) * firstCnt;
        first = x;
    }

    public void pushUp() {
        first = Math.max(left.first, right.first);
        second = Math.max(left.first == first ? left.second : left.first, right.first == first ? right.second : right.first);
        firstCnt = (left.first == first ? left.firstCnt : 0) + (right.first == first ? right.firstCnt : 0);
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        left.setMin(first);
        right.setMin(first);
    }

    public SegmentBeat(int l, int r, IntToLongFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new SegmentBeat(l, m, func);
            right = new SegmentBeat(m + 1, r, func);
            pushUp();
        } else {
            sum = first = func.apply(l);
            second = -inf;
            firstCnt = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateMin(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (first <= x) {
                return;
            }
            if (second < x) {
                setMin(x);
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateMin(ll, rr, l, m, x);
        right.updateMin(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long querySum(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.querySum(ll, rr, l, m) +
                right.querySum(ll, rr, m + 1, r);
    }

    public long queryMax(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -inf;
        }
        if (covered(ll, rr, l, r)) {
            return first;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }

    private SegmentBeat deepClone() {
        SegmentBeat seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected SegmentBeat clone() {
        try {
            return (SegmentBeat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(sum).append(",");
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

