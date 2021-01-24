package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class PrefixSumQueries {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        SumImpl[] sums = new SumImpl[n];
        UpdateImpl upd = new UpdateImpl();
        SumImpl res = new SumImpl();
        for (int i = 0; i < n; i++) {
            sums[i] = new SumImpl();
            upd.v = in.ri();
            sums[i].update(upd);
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> sums[i]);
        debug.debug("st", st);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int a = in.ri();
            int b = in.ri();
            if (t == 1) {
                upd.v = b;
                st.update(a - 1, a - 1, 0, n - 1, upd);
            } else {
                res.sum = res.max = res.pre = 0;
                st.query(a - 1, b - 1, 0, n - 1, res);
                out.println(res.max);
            }
            debug.debug("st", st);
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int v;

    @Override
    public void update(UpdateImpl update) {
        v = update.v;
    }

    @Override
    public void clear() {
        v = Integer.MAX_VALUE;
    }

    @Override
    public boolean ofBoolean() {
        return v != Integer.MAX_VALUE;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long sum;
    long pre;
    long max;


    @Override
    public void add(SumImpl b) {
        pre = Math.max(pre, b.pre + sum);
        max = Math.max(max, pre);
        sum += b.sum;
    }

    @Override
    public void update(UpdateImpl update) {
        sum = update.v;
        pre = max = Math.max(0, sum);
    }

    @Override
    public void copy(SumImpl b) {
        sum = b.sum;
        pre = b.pre;
        max = b.max;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}