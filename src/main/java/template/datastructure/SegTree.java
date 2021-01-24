package template.datastructure;

import template.math.DigitUtils;
import template.utils.Sum;
import template.utils.Update;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class SegTree<S extends Sum<S, U>, U extends Update<U>> implements Cloneable {
    private SegTree<S, U> left;
    private SegTree<S, U> right;
    public S sum;
    private U update;


    private void modify(U x) {
        update.update(x);
        sum.update(x);
    }

    private void pushDown() {
        if (update.ofBoolean()) {
            left.modify(update);
            right.modify(update);
            update.clear();
            assert !update.ofBoolean();
        }
    }

    private void pushUp() {
        sum.copy(left.sum);
        sum.add(right.sum);
    }

    public SegTree(int l, int r, Supplier<S> sSupplier, Supplier<U> uSupplier,
                   IntFunction<S> func) {
        update = uSupplier.get();
        update.clear();
        if (l < r) {
            sum = sSupplier.get();
            int m = DigitUtils.floorAverage(l, r);
            left = new SegTree<>(l, m, sSupplier, uSupplier, func);
            right = new SegTree<>(m + 1, r, sSupplier, uSupplier, func);
            pushUp();
        } else {
            sum = func.apply(l);
        }
    }

    private boolean cover(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return R < l || L > r;
    }

    public void update(int L, int R, int l, int r, U u) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (cover(L, R, l, r)) {
            modify(u);
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown();
        left.update(L, R, l, m, u);
        right.update(L, R, m + 1, r, u);
        pushUp();
    }

    public void query(int L, int R, int l, int r, S s) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (cover(L, R, l, r)) {
            s.add(sum);
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown();
        left.query(L, R, l, m, s);
        right.query(L, R, m + 1, r, s);
    }

    public SegTree<S, U> deepClone() {
        SegTree<S, U> clone = clone();
        clone.sum = clone.sum.clone();
        clone.update = clone.update.clone();
        if (clone.left != null) {
            clone.left = clone.left.deepClone();
            clone.right = clone.right.deepClone();
        }
        return clone;
    }

    public void visitLeave(Consumer<SegTree<S, U>> consumer) {
        if (left == null) {
            consumer.accept(this);
            return;
        }
        pushDown();
        left.visitLeave(consumer);
        right.visitLeave(consumer);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        deepClone().visitLeave(x -> ans.append(x.sum).append(' '));
        return ans.toString();
    }

    @Override
    public SegTree<S, U> clone() {
        try {
            return (SegTree<S, U>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
