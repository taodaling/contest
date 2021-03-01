package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

public class CDestroyingArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        UpdateImpl upd = new UpdateImpl();
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1,
                SumImpl::new, UpdateImpl::new,
                i -> {
                    upd.x = a[i];
                    SumImpl ans = new SumImpl();
                    ans.update(upd);
                    return ans;
                });
        for (int i = 0; i < n; i++){
            int id = in.ri() - 1;
            upd.x = -SumImpl.inf;
            st.update(id, id, 0, n - 1, upd);
            long ans = st.sum.max;
            out.println(ans);
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long x;

    {
        clear();
    }

    @Override
    public void update(UpdateImpl update) {
        x = update.x;
    }

    @Override
    public void clear() {
        x = Long.MAX_VALUE;
    }

    @Override
    public boolean ofBoolean() {
        return x != Long.MAX_VALUE;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long pref;
    long suf;
    long max;
    long sum;

    static long inf = (long) 1e18;

    @Override
    public void add(SumImpl other) {
        max = Math.max(max, other.max);
        max = Math.max(max, suf + other.pref);
        this.pref = Math.max(this.pref, this.sum + other.pref);
        this.suf = Math.max(other.suf, this.suf + other.sum);

        this.sum += other.sum;
        this.sum = Math.max(this.sum, -inf);
    }

    @Override
    public void update(UpdateImpl update) {
        pref = suf = max = Math.max(0, update.x);
        sum = update.x;
    }

    @Override
    public void copy(SumImpl other) {
        sum = other.sum;
        max = other.max;
        pref = other.pref;
        suf = other.suf;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}