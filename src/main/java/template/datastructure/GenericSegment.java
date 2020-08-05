package template.datastructure;

import template.math.DigitUtils;

import java.util.function.IntFunction;
import java.util.function.Supplier;

public class GenericSegment<S extends GenericSegment.Summary<S>, M extends GenericSegment.Modify<S, M>> implements Cloneable {
    private GenericSegment<S, M> left;
    private GenericSegment<S, M> right;
    private S summary;
    private M modify;
    private boolean dirty;

    private void modify(M modify) {
        this.modify.merge(modify);
        modify.modify(summary);
        dirty = true;
    }

    public void pushUp() {
        summary.merge(left.summary, right.summary);
    }

    public void pushDown() {
        if (dirty) {
            left.modify(modify);
            right.modify(modify);
            modify.clear();
            dirty = false;
        }
    }

    public GenericSegment(int l, int r, Supplier<M> modifySupplier, Supplier<S> summarySupplier, IntFunction<S> function) {
        modify = modifySupplier.get();
        if (l < r) {
            summary = summarySupplier.get();
            int m = DigitUtils.floorAverage(l, r);
            left = new GenericSegment<>(l, m, modifySupplier, summarySupplier, function);
            right = new GenericSegment<>(m + 1, r, modifySupplier, summarySupplier, function);
            pushUp();
        } else {
            summary = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, M modify) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(modify);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, modify);
        right.update(ll, rr, m + 1, r, modify);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, S summary) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            summary.merge(this.summary);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, summary);
        right.query(ll, rr, m + 1, r, summary);
    }

    private GenericSegment deepClone() {
        GenericSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected GenericSegment clone() {
        try {
            GenericSegment ans = (GenericSegment) super.clone();
            ans.modify = ans.modify.clone();
            ans.summary = ans.summary.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(summary).append(",");
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

    public static interface Modify<S extends Summary, M extends Modify<S, M>> extends Cloneable {
        void modify(S summary);

        void merge(M modify);

        void clear();

        M clone();
    }

    public static interface Summary<S extends Summary<S>> extends Cloneable {
        void merge(S a);

        void merge(S a, S b);

        S clone();
    }
}
