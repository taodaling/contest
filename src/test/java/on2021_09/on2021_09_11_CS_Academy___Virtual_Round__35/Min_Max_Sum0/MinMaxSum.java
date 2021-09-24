package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Min_Max_Sum0;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.UpdatableSum;
import template.utils.Update;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinMaxSum {
    int mod = (int) 1e9 + 7;
    FastPow2 fp2 = new FastPow2(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Deque<Integer> dq = new ArrayDeque<>(n);
        int[] prevMin = new int[n];
        int[] prevMax = new int[n];
        dq.clear();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && a[dq.peekLast()] >= a[i]) {
                dq.removeLast();
            }
            prevMin[i] = dq.isEmpty() ? -1 : dq.peekLast();
            dq.addLast(i);
        }
        dq.clear();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) {
                dq.removeLast();
            }
            prevMax[i] = dq.isEmpty() ? -1 : dq.peekLast();
            dq.addLast(i);
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1,
                SumImpl::new, UpdateImpl::new, i -> {
            SumImpl s = new SumImpl();
            s.way = way(i);
            s.wayB = s.wayA = a[i] * s.way % mod;
            s.sum = a[i] * s.wayB % mod;
            return s;
        });
        long ans = 0;
        for (int i = 0; i < n; i++) {
            UpdateImpl upd = new UpdateImpl();
            upd.min = a[i];
            upd.max = Integer.MIN_VALUE;
            int firstTrue = prevMin[i] + 1;
            st.update(firstTrue, i - 1, 0, n - 1, upd);

            upd.min = Integer.MAX_VALUE;
            upd.max = a[i];
            firstTrue = prevMax[i] + 1;
            st.update(firstTrue, i - 1, 0, n - 1, upd);

            debug.debug("st", st);
            SumImpl s = new SumImpl();
            st.query(0, i, 0, n - 1, s);
            long contrib = s.sum;
            debug.debug("i", i);
            contrib = contrib * way(n - 1 - i) % mod;
            debug.debug("contrib", contrib);
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);

    public long way(int n) {
        return n == 0 ? 1 : fp2.pow(n - 1);
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int min;
    int max;

    @Override
    public void update(UpdateImpl update) {
        min = Math.min(min, update.min);
        max = Math.max(max, update.max);
    }

    @Override
    public void clear() {
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
    }

    @Override
    public boolean ofBoolean() {
        return !(min == Integer.MAX_VALUE && max == Integer.MIN_VALUE);
    }
}

class SumImpl implements UpdatableSum<SumImpl, UpdateImpl> {
    long sum;
    int mod = (int) 1e9 + 7;
    long wayA;
    long wayB;
    long way;

    @Override
    public void add(SumImpl right) {
        sum += right.sum;
        wayA += right.wayA;
        wayB += right.wayB;
        way += right.way;
        if (sum >= mod) {
            sum -= mod;
        }
        if (wayA >= mod) {
            wayA -= mod;
        }
        if (wayB >= mod) {
            wayB -= mod;
        }
        if (way >= mod) {
            way -= mod;
        }
    }

    @Override
    public void copy(SumImpl right) {
        sum = right.sum;
        way = right.way;
        wayA = right.wayA;
        wayB = right.wayB;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.min != Integer.MAX_VALUE) {
            sum = wayB * update.min % mod;
            wayA = way * update.min % mod;
        }
        if (update.max != Integer.MIN_VALUE) {
            sum = wayA * update.max % mod;
            wayB = way * update.max % mod;
        }
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}