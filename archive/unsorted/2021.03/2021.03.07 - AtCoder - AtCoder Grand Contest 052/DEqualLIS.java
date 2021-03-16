package contest;

import template.datastructure.MonotoneOrderBeta;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerGenericBIT;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.Comparator;

public class DEqualLIS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] perm = in.ri(n);
        int[] prev = new int[n];
        int[] dp = new int[n];
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(1, n,
                SumImpl::new, UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.clear();
            return ans;
        });
        SumImpl bufSum = new SumImpl();
        UpdateImpl bufUpdate = new UpdateImpl();
        for (int i = 0; i < n; i++) {
            bufSum.clear();
            st.query(1, perm[i], 1, n, bufSum);
            dp[i] = bufSum.max + 1;
            int cnt = Math.max(bufSum.cnt, 1);
            prev[i] = bufSum.index;
            bufUpdate.index = i;
            bufUpdate.max = dp[i];
            bufUpdate.cnt = cnt;
            st.update(perm[i], perm[i], 1, n, bufUpdate);
        }

        bufSum.clear();
        st.query(1, n, 1, n, bufSum);
        if (bufSum.cnt > 1 || bufSum.max % 2 == 0) {
            out.println("YES");
            return;
        }
        int lisLen = bufSum.max;
        int cur = bufSum.index;
        boolean[] lis = new boolean[n];
        while (cur != -1) {
            lis[cur] = true;
            cur = prev[cur];
        }
        int[] inc = new int[n];
        int[] dec = new int[n];
        IntegerGenericBIT bit = new IntegerGenericBIT(n, Math::max, 0);
        bit.clear();
        for (int i = 0; i < n; i++) {
            if (lis[i]) {
                continue;
            }
            inc[i] = bit.query(perm[i]) + 1;
            bit.update(perm[i], inc[i]);
        }
        bit.clear();
        for (int i = n - 1; i >= 0; i--) {
            if (lis[i]) {
                continue;
            }
            dec[i] = bit.query(n + 1 - perm[i]) + 1;
            bit.update(n + 1 - perm[i], dec[i]);
        }
        int max = bit.query(n);
        if (max >= (lisLen + 1) / 2) {
            out.println("YES");
            return;
        }

        MonotoneOrderBeta<Integer, Integer> left = new MonotoneOrderBeta<Integer, Integer>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                true, true);
        left.add(0, 0);
        MonotoneOrderBeta<Integer, Integer> right = new MonotoneOrderBeta<Integer, Integer>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                false, true);
        right.add(n + 1, 0);
        int[] a = new int[lisLen];
        int[] b = new int[lisLen];
        int wpos = 0;
        for (int i = 0; i < n; i++) {
            if (lis[i]) {
                a[wpos] = left.floor(perm[i]);
                wpos++;
            } else {
                left.add(perm[i], inc[i]);
            }
        }
        wpos = lisLen - 1;
        for (int i = n - 1; i >= 0; i--) {
            if (lis[i]) {
                b[wpos] = right.ceil(perm[i]);
                wpos--;
            } else {
                right.add(perm[i], dec[i]);
            }
        }


        SegTree<SumImpl2, Update.NilUpdate> st2 = new SegTree<>(0, lisLen - 1, SumImpl2::new,
                Update.NilUpdate.SUPPLIER, i -> {
            SumImpl2 ans = new SumImpl2();
            ans.set(a[i], b[i], i);
            return ans;
        });
        SumImpl2 buf = new SumImpl2();
        int half = lisLen / 2;
        if(half > 0) {
            for (int i = 0; i + half - 1 < lisLen; i++) {
                buf.set(a[i], b[i], i);
                st2.query(i + 1, i + half - 1, 0, lisLen - 1, buf);
                if (buf.max > half) {
                    out.println("YES");
                    return;
                }
            }
        }
        out.println("NO");
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int cnt;
    int max;
    int index;

    @Override
    public void update(UpdateImpl update) {
        if (max < update.max) {
            max = update.max;
            cnt = 0;
            index = -1;
        }
        if (max == update.max) {
            cnt += update.cnt;
            index = update.index;
        }
        cnt = Math.min(cnt, 2);
    }

    @Override
    public void clear() {
        cnt = 0;
        max = 0;
        index = -1;
    }

    @Override
    public boolean ofBoolean() {
        return cnt != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int cnt;
    int max;
    int index;

    void clear() {
        cnt = 0;
        max = 0;
        index = -1;
    }

    @Override
    public void add(SumImpl sum) {
        if (max < sum.max) {
            max = sum.max;
            cnt = 0;
            index = -1;
        }
        if (max == sum.max) {
            cnt += sum.cnt;
            index = sum.index;
        }
        cnt = Math.min(cnt, 2);
    }

    @Override
    public void update(UpdateImpl sum) {
        if (max < sum.max) {
            max = sum.max;
            cnt = 0;
            index = -1;
        }
        if (max == sum.max) {
            cnt += sum.cnt;
            index = sum.index;
        }
        cnt = Math.min(cnt, 2);
    }

    @Override
    public void copy(SumImpl sum) {
        this.index = sum.index;
        this.max = sum.max;
        this.cnt = sum.cnt;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "(" + index + "," + max + ")";
    }
}

class SumImpl2 implements Sum<SumImpl2, Update.NilUpdate> {
    int left;
    int right;
    int max;

    @Override
    public void add(SumImpl2 sumImpl2) {
        max = Math.max(max, sumImpl2.max);
        max = Math.max(max, left + sumImpl2.right);
        left = Math.max(left, sumImpl2.max);
        right = Math.max(right, sumImpl2.right);
    }

    @Override
    public void update(Update.NilUpdate nilUpdate) {

    }

    public void set(int a, int b, int i) {
        left = a - i;
        right = b + i + 1;
        max = right + left;
    }

    @Override
    public void copy(SumImpl2 sumImpl2) {
        this.max = sumImpl2.max;
        this.left = sumImpl2.left;
        this.right = sumImpl2.right;
    }

    @Override
    public SumImpl2 clone() {
        SumImpl2 ans = new SumImpl2();
        ans.copy(this);
        return ans;
    }
}
