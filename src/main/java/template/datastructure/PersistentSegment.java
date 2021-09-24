package template.datastructure;

import template.math.DigitUtils;

public class PersistentSegment implements Cloneable {
    static final PersistentSegment NIL = new PersistentSegment();
    PersistentSegment left;
    PersistentSegment right;

    public void pushUp() {
    }

    public void pushDown() {
        left = left.clone();
        right = right.clone();
    }

    public PersistentSegment() {
        left = right = this;
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
        left.update(ll, rr, l, m);
        right.update(ll, rr, m + 1, r);
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
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    @Override
    public PersistentSegment clone() {
        try {
            return (PersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }



    private void toString(StringBuilder builder) {
        if (left == NIL && right == NIL) {
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
        clone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
