package template.primitve.generated.graph;

import template.math.DigitUtils;

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
            int m = DigitUtils.floorAverage(l, r);
            left = new DoublePriorityQueueBasedOnSegment(l, m);
            right = new DoublePriorityQueueBasedOnSegment(m + 1, r);
            pushUp();
        } else {

        }
    }

    public void reset(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.reset(l, m);
            right.reset(m + 1, r);
            pushUp();
        } else {
            minimum = INF;
        }
    }

    public void update(int x, int l, int r, double val) {
        if (l > x || r < x) {
            return;
        }
        if (l == r) {
            minimum = val;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        left.update(x, l, m, val);
        right.update(x, m + 1, r, val);
        pushUp();
    }

    public int pop(int l, int r) {
        if (l == r) {
            minimum = INF;
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int ans;
        if (left.minimum == minimum) {
            ans = left.pop(l, m);
        } else {
            ans = right.pop(m + 1, r);
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