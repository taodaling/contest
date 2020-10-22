package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.io.PrintWriter;

public class FIHateShortestPathProblem {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int h = in.readInt();
        int w = in.readInt();
        Segment segment = new Segment(1, w);

        debug.debug("seg", segment);
        for (int i = 0; i < h; i++) {
            int l = in.readInt();
            int r = in.readInt();
            segment.update(r + 1, r + 1, 1, w, segment.queryNeg(l, r, 1, w) + (r + 1));
            segment.updateInf(l, r, 1, w);

            long ans = segment.queryMinVal(1, w, 1, w) + i + 1;
            if (ans > 1e17) {
                out.println(-1);
            } else {
                out.println(ans);
            }
            debug.debug("seg", segment);
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long val;
    long minPlusNegX;
    long minPlusPosX;
    int index;
    boolean setInf;
    static long inf = (long) 1e18;
    long minVal;


    private void modify(long x) {
        minVal = val = Math.min(x, val);
        minPlusNegX = val - index;
        minPlusPosX = val + index;
    }

    private void setInf() {
        minVal = val = minPlusNegX = minPlusPosX = inf;
        setInf = true;
    }

    public void pushUp() {
        minPlusPosX = Math.min(left.minPlusPosX, right.minPlusPosX);
        minPlusNegX = Math.min(left.minPlusNegX, right.minPlusNegX);
        minVal = Math.min(left.minVal, right.minVal);
    }

    public void pushDown() {
        if (setInf) {
            left.setInf();
            right.setInf();
            setInf = false;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            index = l;
            modify(0);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long val) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(val);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, val);
        right.update(ll, rr, m + 1, r, val);
        pushUp();
    }

    public void updateInf(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setInf();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateInf(ll, rr, l, m);
        right.updateInf(ll, rr, m + 1, r);
        pushUp();
    }

    public long queryMinVal(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return minVal;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryMinVal(ll, rr, l, m),
                right.queryMinVal(ll, rr, m + 1, r));
    }


    public long queryNeg(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return minPlusNegX;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryNeg(ll, rr, l, m),
                right.queryNeg(ll, rr, m + 1, r));
    }

    public long queryPos(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return minPlusPosX;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryPos(ll, rr, l, m),
                right.queryPos(ll, rr, m + 1, r));
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
            builder.append(minVal).append(",");
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