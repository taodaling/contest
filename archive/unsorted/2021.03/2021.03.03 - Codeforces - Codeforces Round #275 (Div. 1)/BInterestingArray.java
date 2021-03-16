package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class BInterestingArray {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(1, n,
                SumImpl::new, UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            return ans;
        });

        UpdateImpl bufUpd = new UpdateImpl();
        SumImpl bufSum = new SumImpl();

        int[][] limits = new int[m][3];
        for (int i = 0; i < m; i++) {
            limits[i] = new int[]{in.ri(), in.ri(), in.ri()};
        }
        for (int i = 0; i < m; i++) {
            int l = limits[i][0];
            int r = limits[i][1];
            bufUpd.or = limits[i][2];
            st.update(l, r, 1, n, bufUpd);
            debug.debug("st", st);
        }
        for (int i = 0; i < m; i++) {
            int l = limits[i][0];
            int r = limits[i][1];
            bufSum.and = ~0;
            st.query(l, r, 1, n, bufSum);
            if (bufSum.and != limits[i][2]) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
        st.visitLeave(leaf -> {
            out.append(leaf.sum.and).append(' ');
        });
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int or;

    @Override
    public void update(UpdateImpl update) {
        or |= update.or;
    }

    @Override
    public void clear() {
        or = 0;
    }

    @Override
    public boolean ofBoolean() {
        return or != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int and;

    @Override
    public void add(SumImpl sum) {
        and &= sum.and;
    }

    @Override
    public void update(UpdateImpl update) {
        and |= update.or;
    }

    @Override
    public void copy(SumImpl sum) {
        and = sum.and;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + and;
    }
}
