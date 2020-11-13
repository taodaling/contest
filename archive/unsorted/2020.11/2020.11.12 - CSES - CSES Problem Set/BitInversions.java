package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;
import template.utils.Update;

public class BitInversions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.readString(s, 0);
        SegTree<SumImpl, UpdateImpl> a = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.set(s[i] == '0');
                    return ans;
                });
        SegTree<SumImpl, UpdateImpl> b = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.set(s[i] == '1');
                    return ans;
                });
        UpdateImpl upd = new UpdateImpl();

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int x = in.readInt() - 1;
            upd.inv = true;
            a.update(x, x, 0, n - 1, upd);
            b.update(x, x, 0, n - 1, upd);
            int ans = Math.max(a.sum.max, b.sum.max);
            out.println(ans);
        }
    }
}

class UpdateImpl implements Update<UpdateImpl> {
    boolean inv;

    @Override
    public void update(UpdateImpl update) {
        if (update.inv) {
            inv = !inv;
        }
    }

    @Override
    public void clear() {
        inv = false;
    }

    @Override
    public boolean ofBoolean() {
        return inv;
    }

    @Override
    public UpdateImpl clone() {
        UpdateImpl ans = new UpdateImpl();
        ans.inv = inv;
        return ans;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int max;
    int pre;
    int post;
    boolean fill;

    @Override
    public void add(SumImpl right) {
        max = Math.max(max, right.max);
        max = Math.max(post + right.pre, max);
        if (fill) {
            pre += right.pre;
        }
        if (right.fill) {
            post = right.post + post;
        } else {
            post = right.post;
        }
        fill = fill && right.fill;
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.inv) {
            set(!fill);
        }
    }

    public void set(boolean x) {
        fill = x;
        max = pre = post = fill ? 1 : 0;
    }

    @Override
    public void copy(SumImpl sum) {
        fill = sum.fill;
        pre = sum.pre;
        post = sum.post;
        max = sum.max;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}