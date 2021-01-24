package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;
import template.utils.Update;

public class DProgram {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = in.rc() == '-' ? -1 : 1;
        }
        SegTree<SumImpl, Update.NilUpdate> st = new SegTree<>(0, n - 1, SumImpl::new,
                Update.NilUpdate.SUPPLIER, i -> {
            SumImpl ans = new SumImpl();
            ans.set(v[i]);
            return ans;
        });
        for (int i = 0; i < m; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            SumImpl ans = new SumImpl();
            st.query(0, l - 1, 0, n - 1, ans);
            st.query(r + 1, n - 1, 0, n - 1, ans);
            out.println(ans.r - ans.l + 1);
        }
    }
}

class SumImpl implements Sum<SumImpl, Update.NilUpdate> {
    int l;
    int r;
    int s;

    @Override
    public void add(SumImpl sum) {
        l = Math.min(l, sum.l + s);
        r = Math.max(r, sum.r + s);
        s += sum.s;
    }

    @Override
    public void update(Update.NilUpdate nilUpdate) {

    }

    public void set(int x) {
        s = x;
        l = Math.min(0, x);
        r = Math.max(0, x);
    }

    @Override
    public void copy(SumImpl sum) {
        l = sum.l;
        r = sum.r;
        s = sum.s;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}