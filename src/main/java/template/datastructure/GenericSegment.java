package template.datastructure;

import template.math.DigitUtils;

import java.util.function.IntFunction;
import java.util.function.Supplier;

public class GenericSegment<S extends GenericSegment.Sum<S, U>, U extends GenericSegment.Update<U>> implements Cloneable {
    private GenericSegment<S, U> left;
    private GenericSegment<S, U> right;
    private S sum;
    private U update;
    private boolean dirty;

    private void modify(U modify) {
        update.update(modify);
        sum.update(modify);
        dirty = true;
    }

    public void pushUp() {
        sum.merge(left.sum, right.sum);
    }

    public void pushDown() {
        if (dirty) {
            left.modify(update);
            right.modify(update);
            update.clear();
            dirty = false;
        }
    }

    public GenericSegment(int l, int r, Supplier<U> modifySupplier, Supplier<S> summarySupplier, IntFunction<S> function) {
        update = modifySupplier.get();
        if (l < r) {
            sum = summarySupplier.get();
            int m = DigitUtils.floorAverage(l, r);
            left = new GenericSegment<>(l, m, modifySupplier, summarySupplier, function);
            right = new GenericSegment<>(m + 1, r, modifySupplier, summarySupplier, function);
            pushUp();
        } else {
            sum = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, U modify) {
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
            summary.add(this.sum);
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
            ans.update = (U) ans.update.clone();
            ans.sum = ans.sum.clone();
            return ans;
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

    public static interface Update<U> extends Cloneable {
        void update(U u);

        void clear();

        U clone();
    }

    public static interface Sum<S extends Sum<S, U>, U extends Update<U>> extends Cloneable {
        void add(S a);

        void merge(S a, S b);

        void update(U u);

        S clone();
    }
}
