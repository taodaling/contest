package template.primitve.generated.graph;

public class DoublePriorityQueueBasedOnSegment implements Cloneable {
    private static final double INF = Double.MAX_VALUE;
    private DoublePriorityQueueBasedOnSegment left;
    private DoublePriorityQueueBasedOnSegment right;
    private double minimum;

    public void pushUp() {
        minimum = Math.min(left.minimum, right.minimum);
    }

    public DoublePriorityQueueBasedOnSegment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new DoublePriorityQueueBasedOnSegment(l, m);
            right = new DoublePriorityQueueBasedOnSegment(m + 1, r);
            pushUp();
        } else {

        }
    }

    public void reset(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left.reset(l, m);
            right.reset(m + 1, r);
            pushUp();
        } else {
            minimum = INF;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, double val) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            minimum = val;
            return;
        }
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, val);
        right.update(ll, rr, m + 1, r, val);
        pushUp();
    }

    public int query(int l, int r) {
        if (l == r) {
            minimum = INF;
            return l;
        }
        int m = (l + r) >> 1;
        int ans;
        if (left.minimum == minimum) {
            ans = left.query(l, m);
        } else {
            ans = right.query(m + 1, r);
        }
        pushUp();
        return ans;
    }

    private DoublePriorityQueueBasedOnSegment deepClone() {
        DoublePriorityQueueBasedOnSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    protected DoublePriorityQueueBasedOnSegment clone() {
        try {
            return (DoublePriorityQueueBasedOnSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(minimum).append(",");
            return;
        }
        left.toString(builder);
        right.toString(builder);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}