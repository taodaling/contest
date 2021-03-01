package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Pair;
import template.utils.Sum;
import template.utils.Update;

public class UTSOpen21P5StateTaxes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1,
                SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.sum = 0;
                    ans.b = a[i];
                    return ans;
                });
        UpdateImpl upd = new UpdateImpl();
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            if (t == 1) {
                int x = in.ri();
                upd.clear();
                upd.b = x;
            } else {
                upd.clear();
                upd.sumA = 1;
            }
            st.update(l, r, 0, n - 1, upd);
        }
        st.visitLeave(leaf -> {
            out.append(leaf.sum.sum).append(' ');
        });
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long sumA;
    long sumB;
    long b;

    @Override
    public void update(UpdateImpl update) {
        sumA += update.sumA;
        sumB += update.sumA * b + update.sumB;
        b += update.b;
    }

    @Override
    public void clear() {
        sumA = sumB = b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(sumA == 0 && sumB == 0 && b == 0);
    }

    @Override
    public UpdateImpl clone() {
        return null;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long b;
    long sum;

    @Override
    public void add(SumImpl sum) {
        b += sum.b;
        this.sum += sum.sum;
    }

    @Override
    public void update(UpdateImpl update) {
        sum += b * update.sumA + update.sumB;
        b += update.b;
    }

    @Override
    public void copy(SumImpl sum) {
        b = sum.b;
        this.sum = sum.sum;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}