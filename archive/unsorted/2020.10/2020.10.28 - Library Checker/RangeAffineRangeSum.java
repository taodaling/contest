package contest;

import template.datastructure.Segtree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class RangeAffineRangeSum {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Segtree<SumImpl, UpdateImpl> seg = new Segtree<>(0, n - 1, SumImpl::new, UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.sum = a[i];
            ans.size = 1;
            return ans;
        });

        UpdateImpl u = new UpdateImpl();
        SumImpl s = new SumImpl();
        debug.debug("seg", seg);
        for(int i = 0; i < q; i++){
            int t = in.readInt();
            int l = in.readInt();
            int r = in.readInt() - 1;
            if(t == 0){
                u.a = in.readLong();
                u.b = in.readLong();
                seg.update(l, r, 0, n - 1, u);
            }else{
                s.sum = 0;
                s.size = 0;
                seg.query(l, r, 0, n - 1, s);
                out.println(s.sum);
            }
            debug.debug("seg", seg);
        }

    }
}

class UpdateImpl implements Update<UpdateImpl> {
    static int mod = 998244353;
    long a = 1;
    long b = 0;

    @Override
    public void update(UpdateImpl update) {
        a = a * update.a % mod;
        b = (update.a * b + update.b) % mod;
    }

    @Override
    public void clear() {
        a = 1;
        b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(a == 1 && b == 0);
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
    static int mod = 998244353;
    long sum;
    long size;

    @Override
    public void add(SumImpl s) {
        sum = DigitUtils.modplus(sum, s.sum, mod);
        size += s.size;
    }

    @Override
    public void update(UpdateImpl u) {
        sum = (sum * u.a + size * u.b) % mod;
    }

    @Override
    public void copy(SumImpl s) {
        sum = s.sum;
        size = s.size;
    }

    @Override
    public SumImpl clone() {
        SumImpl sum = new SumImpl();
        sum.copy(this);
        return sum;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}
