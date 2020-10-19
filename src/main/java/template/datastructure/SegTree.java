package template.datastructure;

import template.binary.Log2;
import template.math.DigitUtils;
import template.utils.AlgebraController;
import template.utils.CloneSupportObject;

import java.util.function.IntFunction;

public class SegTree<S, D> implements Cloneable {
    private Object[] sum;
    private Object[] dirty;
    private int L;
    private int R;
    private AlgebraController<S, D> sc;
    private AlgebraController<D, D> dc;

    public SegTree(int n, AlgebraController<S, D> sc, AlgebraController<D, D> dc) {
        this.sc = sc;
        this.dc = dc;
        int size = 1 << Log2.ceilLog(n) + 1;
        sum = new Object[size];
        dirty = new Object[size];
        for (int i = 0; i < size; i++) {
            sum[i] = sc.alloc();
            dirty[i] = dc.alloc();
        }
    }

    private void modify(int i, D d) {
        dc.modify((D) dirty[i], d);
        sc.modify((S) sum[i], d);
    }

    public void pushUp(int i) {
        sum[i] = sc.replace(sc.plus((S) sum[left(i)], (S) sum[right(i)]), (S) sum[i]);
    }

    public void pushDown(int i) {
        if (dc.ofBoolean((D) dirty[i])) {
            modify(left(i), (D) dirty[i]);
            modify(right(i), (D) dirty[i]);
            dirty[i] = dc.replace(dc.alloc(), (D) dirty[i]);
        }
    }

    private int left(int i) {
        return (i << 1);
    }

    private int right(int i) {
        return (i << 1) | 1;
    }

    public void init(int i, int l, int r, IntFunction<S> function) {
        if (i == 1) {
            L = l;
            R = r;
        }
        dirty[i] = dc.replace(dc.alloc(), (D) dirty[i]);
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            init(left(i), l, m, function);
            init(right(i), m + 1, r, function);
            pushUp(i);
        } else {
            sum[i] = sc.replace(function.apply(l), (S) sum[i]);
        }
    }

    private boolean cover(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int i, int ll, int rr, int l, int r, D d) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (cover(ll, rr, l, r)) {
            modify(i, d);
            return;
        }
        pushDown(i);
        int m = DigitUtils.floorAverage(l, r);
        update(left(i), ll, rr, l, m, d);
        update(right(i), ll, rr, m + 1, r, d);
        pushUp(i);
    }

    public S query(int i, int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r)) {
            return sc.alloc();
        }
        if (cover(ll, rr, l, r)) {
            return sc.clone((S) sum[i]);
        }
        pushDown(i);
        int m = DigitUtils.floorAverage(l, r);
        S a = query(left(i), ll, rr, l, m);
        S b = query(right(i), ll, rr, m + 1, r);
        return sc.replace(sc.plus(a, b), a, b);
    }

    @Override
    protected SegTree<S, D> clone() {
        try {
            SegTree<S, D> ans = (SegTree<S, D>) super.clone();
            ans.sum = ans.sum.clone();
            ans.dirty = ans.dirty.clone();
            for (int i = 0; i < ans.sum.length; i++) {
                ans.sum[i] = sc.clone((S) ans.sum[i]);
                ans.dirty[i] = dc.clone((D) ans.dirty[i]);
            }
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder, int i, int l, int r) {
        if (l == r) {
            builder.append(sum[i]).append(",");
            return;
        }
        pushDown(i);
        int m = DigitUtils.floorAverage(l, r);
        toString(builder, left(i), l, m);
        toString(builder, right(i), m + 1, r);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        clone().toString(builder, 1, L, R);
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }

}
