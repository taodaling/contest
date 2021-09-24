package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.E__Equilibrium;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.UpdatableSum;
import template.utils.Update;

public class EEquilibrium {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] ab = new long[n];
        for (int i = 0; i < n; i++) {
            ab[i] -= in.ri();
        }
        for (int i = 0; i < n; i++) {
            ab[i] += in.ri();
        }
        SegTree<SumImpl, Update.NIL> st = new SegTree<>(0, n - 1, SumImpl::new,
                Update.NIL.SUPPLIER,
                i -> {
                    SumImpl s = new SumImpl();
                    s.maxPs = Math.max(0, ab[i]);
                    s.minPs = Math.min(0, ab[i]);
                    s.ps = ab[i];
                    return s;
                });


        debug.debug("ab", ab);
        SumImpl sum = new SumImpl();
        for (int i = 0; i < q; i++) {
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            sum.clear();
            st.query(l, r, 0, n - 1, sum);
            if (sum.minPs < 0 || sum.ps != 0) {
                out.println(-1);
            } else {
                out.println(sum.maxPs);
            }
        }
    }

    Debug debug = new Debug(false);
}

class SumImpl implements UpdatableSum<SumImpl, Update.NIL> {
    long ps;
    long minPs;
    long maxPs;

    void clear() {
        ps = minPs = maxPs = 0;
    }

    @Override
    public void add(SumImpl right) {
        minPs = Math.min(minPs, ps + right.minPs);
        maxPs = Math.max(maxPs, ps + right.maxPs);
        ps += right.ps;
    }

    @Override
    public void copy(SumImpl right) {
        ps = right.ps;
        minPs = right.minPs;
        maxPs = right.maxPs;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public void update(Update.NIL nil) {

    }

    @Override
    public String toString() {
        return "" + ps;
    }
}