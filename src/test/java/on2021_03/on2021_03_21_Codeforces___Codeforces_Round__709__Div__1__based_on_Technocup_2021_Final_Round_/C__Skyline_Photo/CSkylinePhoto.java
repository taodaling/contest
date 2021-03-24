package on2021_03.on2021_03_21_Codeforces___Codeforces_Round__709__Div__1__based_on_Technocup_2021_Final_Round_.C__Skyline_Photo;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Pair;
import template.utils.Sum;
import template.utils.Update;

public class CSkylinePhoto {
    static long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] h = in.ri(n);
        int[] b = in.ri(n);
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            prev[i] = i - 1;
            while (prev[i] >= 0 && h[prev[i]] > h[i]) {
                prev[i] = prev[prev[i]];
            }
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n,
                SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.a = -inf;
                    ans.max = -inf;
                    return ans;
                });
        UpdateImpl upd = new UpdateImpl();
        SumImpl sum = new SumImpl();
        upd.setA = 0;
        upd.setB = 0;
        st.update(0, 0, 0, n, upd);
        for (int i = 0; i < n; i++) {
            int last = prev[i] + 1;
            upd.clear();
            upd.setB = b[i];
            st.update(last, i, 0, n, upd);
            sum.clear();
            st.query(0, i, 0, n, sum);
            if (i == n - 1) {
                out.println(sum.max);
                return;
            }
            upd.setA = sum.max;
            upd.setB = 0;
            st.update(i + 1, i + 1, 0, n, upd);
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    static long unset = Long.MIN_VALUE;
    long setA;
    long setB;

    @Override
    public void update(UpdateImpl update) {
        if (update.setA != unset) {
            setA = update.setA;
        }
        if (update.setB != unset) {
            setB = update.setB;
        }
    }

    @Override
    public void clear() {
        setA = setB = unset;
    }

    @Override
    public boolean ofBoolean() {
        return !(setA == unset && setB == unset);
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long a;
    long max;
    static long inf = (long) 1e18;

    public void clear() {
        a = max = -inf;
    }

    @Override
    public void add(SumImpl sum) {
        a = Math.max(a, sum.a);
        max = Math.max(max, sum.max);
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.setA != UpdateImpl.unset) {
            a = update.setA;
        }
        if (update.setB != UpdateImpl.unset) {
            max = a + update.setB;
        }
    }

    @Override
    public void copy(SumImpl sum) {
        a = sum.a;
        max = sum.max;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return this;
    }
}
