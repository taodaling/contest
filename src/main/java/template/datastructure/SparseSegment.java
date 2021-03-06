package template.datastructure;

import template.math.DigitUtils;

public class SparseSegment {
    private SparseSegment left = NIL;
    private SparseSegment right = NIL;
    public static final SparseSegment NIL = new SparseSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private SparseSegment getLeft() {
        if (left == NIL) {
            left = new SparseSegment();
        }
        return left;
    }

    private SparseSegment getRight() {
        if (right == NIL) {
            right = new SparseSegment();
        }
        return right;
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public SparseSegment(int l, int r) {
    }

    public SparseSegment() {
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        getLeft().update(ll, rr, l, m);
        getRight().update(ll, rr, m + 1, r);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        getLeft().query(ll, rr, l, m);
        getRight().query(ll, rr, m + 1, r);
    }
}
