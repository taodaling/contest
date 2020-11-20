package on2020_11.on2020_11_19_Luogu.P4314_CPU__;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class P4314CPU {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(1, n, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl sum = new SumImpl();
                    sum.x = sum.y = a[i - 1];
                    return sum;
                });
        int q = in.readInt();
        SumImpl sum = new SumImpl();
        UpdateImpl update = new UpdateImpl();
        debug.debug("st", st);
        for (int i = 0; i < q; i++) {
            char c = in.rc();
            if (c == 'Q' || c == 'A') {
                int x = in.ri();
                int y = in.ri();
                assert x <= y;
                sum.x = sum.y = Integer.MIN_VALUE;
                st.query(x, y, 1, n, sum);
                if (c == 'Q') {
                    out.println(sum.x);
                } else {
                    out.println(sum.y);
                }
            } else {
                update.clear();
                int x = in.ri();
                int y = in.ri();
                int z = in.ri();

                assert x <= y;
                if (c == 'P') {
                    update.a = 1;
                    update.b = z;
                    update.aa = z;
                    update.bb = Integer.MIN_VALUE;
                } else {
                    update.a = 0;
                    update.b = z;
                    update.aa = 0;
                    update.bb = z;
                }
                st.update(x, y, 1, n, update);
            }
            debug.debug("st", st);
        }

        return;
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long a;
    long b;
    long aa;
    long bb;

    {
        clear();
    }

    @Override
    public void update(UpdateImpl update) {
        if (a == 0) {
            bb = Math.max(bb, b + update.aa);
        }
        bb = Math.max(bb, update.bb);
        if (a == 1) {
            aa = Math.max(aa, b + update.aa);
        }
        a = a * update.a;
        b = update.a * b + update.b;
    }

    @Override
    public void clear() {
        a = 1;
        b = 0;
        aa = 0;
        bb = Integer.MIN_VALUE;
    }

    @Override
    public boolean ofBoolean() {
        return !(a == 1 && b == 0 && aa == 0 && bb == Integer.MIN_VALUE);
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long x;
    long y;

    @Override
    public void add(SumImpl sum) {
        x = Math.max(x, sum.x);
        y = Math.max(y, sum.y);
    }

    @Override
    public void update(UpdateImpl update) {
        y = Math.max(y, x + update.aa);
        y = Math.max(y, update.bb);
        x = update.a * x + update.b;

        assert x <= y;
    }

    @Override
    public void copy(SumImpl sum) {
        x = sum.x;
        y = sum.y;
    }

    @Override
    public SumImpl clone() {
        SumImpl sum = new SumImpl();
        sum.copy(this);
        return sum;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
