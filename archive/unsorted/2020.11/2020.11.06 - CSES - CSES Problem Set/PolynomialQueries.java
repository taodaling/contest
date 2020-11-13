package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;
import template.utils.Update;

public class PolynomialQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        SegTree<SumImpl, UpdateImpl> seg = new SegTree<>(
                0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.size = 1;
                    ans.sum = a[i];
                    ans.x = i;
                    return ans;
                }
        );

        UpdateImpl upd = new UpdateImpl();
        SumImpl sum = new SumImpl();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            if (t == 1) {
                upd.a = 1;
                upd.b = -(l - 1);
                seg.update(l, r, 0, n - 1, upd);
            } else {
                sum.x = sum.sum = sum.size = 0;
                seg.query(l, r, 0, n - 1, sum);
                out.println(sum.sum);
            }
        }
    }
}

class UpdateImpl implements Update<UpdateImpl> {
    long a;
    long b;

    @Override
    public void update(UpdateImpl update) {
        a = a + update.a;
        b = b + update.b;
    }

    @Override
    public void clear() {
        a = 0;
        b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(a == 0 && b == 0);
    }

    @Override
    public UpdateImpl clone() {
        UpdateImpl ans = new UpdateImpl();
        ans.a = a;
        ans.b = b;
        return ans;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long sum;
    long x;
    int size;


    @Override
    public void add(SumImpl right) {
        this.sum += right.sum;
        this.size += right.size;
        this.x += right.x;
    }

    @Override
    public void update(UpdateImpl update) {
        sum += update.a * x + update.b * size;
    }

    @Override
    public void copy(SumImpl sum) {
        this.size = sum.size;
        this.sum = sum.sum;
        this.x = sum.x;
    }

    @Override
    public SumImpl clone() {
        SumImpl sum = new SumImpl();
        sum.copy(this);
        return sum;
    }
}