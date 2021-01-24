package template.datastructure;

import template.math.DigitUtils;

public class Segment implements Cloneable {
    private Segment left;
    private Segment right;

    private void modify() {
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m);
        right.update(L, R, m + 1, r);
        pushUp();
    }

    public void query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(L, R, l, m);
        right.query(L, R, m + 1, r);
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

