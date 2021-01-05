package contest;

import template.binary.Log2;
import template.datastructure.LinearBasis;
import template.datastructure.SegTree;
import template.datastructure.XorBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.Arrays;

public class EDuffAsAQueen {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = new int[n];
        in.populate(a);
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = a[i];
            if (i > 0) {
                b[i] ^= a[i - 1];
            }
        }
        XorBIT bit = new XorBIT(n + 1);
        for (int i = 0; i < n; i++) {
            bit.update(i + 1, b[i]);
        }
        UpdateImpl upd = new UpdateImpl();
        SumImpl query = new SumImpl();
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl sum = new SumImpl();
                    upd.x = b[i];
                    sum.update(upd);
                    return sum;
                });
        //st.toString();

        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            if (t == 1) {
                int x = in.ri();
                bit.update(l + 1, x);
                bit.update(r + 2, x);
                upd.x = x;
                st.update(l, l, 0, n - 1, upd);
                st.update(r + 1, r + 1, 0, n - 1, upd);
            } else {
                int al = (int) bit.query(l + 1);
                query.clear();
                st.query(l + 1, r, 0, n - 1, query);
                IntLinearBasis lb = query.lb;
                lb.add(al);
                out.println(lb.xorNumberCount());
            }
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int x;

    @Override
    public void update(UpdateImpl update) {
        x ^= update.x;
    }

    @Override
    public void clear() {
        x = 0;
    }

    @Override
    public boolean ofBoolean() {
        return x != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    IntLinearBasis lb = new IntLinearBasis();
    int val;

    public void clear() {
        lb.clear();
    }

    @Override
    public void add(SumImpl sum) {
        for (int x : sum.lb.map) {
            lb.add(x);
        }
    }

    @Override
    public void update(UpdateImpl update) {
        lb.clear();
        val ^= update.x;
        lb.add(val);
    }

    @Override
    public void copy(SumImpl sum) {
        lb.copy(sum.lb);
        val = sum.val;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + lb;
    }
}

class IntLinearBasis {
    int[] map = new int[30];
    /**
     * map[i] = xor of source[i]
     */
    private int set;

    public int size() {
        return Long.bitCount(set);
    }

    public void clear() {
        set = 0;
        Arrays.fill(map, 0);
    }


    public int[] toArray() {
        int[] ans = new int[size()];
        int tail = 0;
        for (int i = 29; i >= 0; i--) {
            if (map[i] != 0) {
                ans[tail++] = map[i];
            }
        }
        return ans;
    }

    /**
     * return the index of  added element ([0,30)), -1 means can't add val
     *
     * @param val
     * @return
     */
    public int add(int val) {
        for (int i = 29; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0 || map[i] == 0) {
                continue;
            }
            val ^= map[i];
        }
        if (val != 0) {
            int log = Log2.floorLog(val);
            map[log] = val;
            set |= 1 << log;
            return log;
        }
        return -1;
    }

    private int bitAt(int val, int i) {
        return (val >>> i) & 1;
    }

    public int xorNumberCount() {
        return 1 << size();
    }

    public void copy(IntLinearBasis model) {
        System.arraycopy(model.map, 0, map, 0, map.length);
        set = model.set;
    }

    @Override
    public IntLinearBasis clone() {
        try {
            IntLinearBasis ans = (IntLinearBasis) super.clone();
            ans.map = ans.map.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
