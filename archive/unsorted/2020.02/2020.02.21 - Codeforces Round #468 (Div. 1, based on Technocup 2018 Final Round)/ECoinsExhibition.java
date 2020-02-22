package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import javax.security.auth.callback.CallbackHandler;
import java.util.Arrays;

public class ECoinsExhibition {
    private static Modular modular = new Modular(1e9 + 7);
    private static CachedPow pow = new CachedPow(2, modular);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int n = in.readInt();
        int m = in.readInt();
        Interval[] one = new Interval[n];
        Interval[] zero = new Interval[m];
        IntegerList list = new IntegerList((n + m) * 4 + 3);
        list.add(1);
        list.add(0);
        list.add(k);
        for (int i = 0; i < n; i++) {
            one[i] = new Interval();
            one[i].l = in.readInt();
            one[i].r = in.readInt();
            list.add(one[i].l);
            list.add(one[i].r);
            list.add(one[i].l - 1);
            list.add(one[i].r - 1);
        }
        for (int i = 0; i < m; i++) {
            zero[i] = new Interval();
            zero[i].l = in.readInt();
            zero[i].r = in.readInt();
            list.add(zero[i].l);
            list.add(zero[i].r);
            list.add(zero[i].l - 1);
            list.add(zero[i].r - 1);
        }
        Arrays.sort(one, (a, b) -> Integer.compare(a.r, b.r));
        Arrays.sort(zero, (a, b) -> Integer.compare(a.r, b.r));
        SimplifiedDeque<Interval> dq0 = new Range2DequeAdapter<>(i -> zero[i], 0, m - 1);
        SimplifiedDeque<Interval> dq1 = new Range2DequeAdapter<>(i -> one[i], 0, n - 1);
        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());

        k = dm.maxRank();
        Segment[] segs = new Segment[2];
        segs[0] = new Segment(0, k);
        segs[1] = new Segment(0, k);
        segs[0].updateVal(0, 0, 0, k, 1);
        segs[1].updateVal(0, 0, 0, k, 1);
        for (int i = 1; i <= k; i++) {
            if (i > 1) {
                int sum0 = segs[0].query(0, i - 1, 0, k);
                int sum1 = segs[1].query(0, i - 1, 0, k);
                int diff = dm.iThElement(i) - dm.iThElement(i - 1);
                if (diff == 1) {
                    segs[0].updateVal(i - 1, i - 1, 0, k, sum1);
                    segs[1].updateVal(i - 1, i - 1, 0, k, sum0);
                } else if (diff > 1) {
                    int s = modular.plus(sum0, sum1);
                    int time = pow.pow(diff - 1);
                    int v = modular.mul(s, time - 1);
                    segs[0].updateVal(i, i, 0, k, modular.plus(v, sum1));
                    segs[1].updateVal(i, i, 0, k, modular.plus(v, sum0));
                }
            }
            while (!dq0.isEmpty() && dq0.peekFirst().r == dm.iThElement(i)) {
                Interval head = dq0.removeFirst();
                segs[0].updateClear(0, dm.rankOf(head.l - 1), 0, k);
            }
            while (!dq1.isEmpty() && dq1.peekFirst().r == dm.iThElement(i)) {
                Interval head = dq1.removeFirst();
                segs[1].updateClear(0, dm.rankOf(head.l - 1), 0, k);
            }
        }

        int ans0 = segs[0].query(0, k, 0, k);
        int ans1 = segs[1].query(0, k, 0, k);
        int ans = modular.plus(ans0, ans1);
        out.println(ans);
    }
}

class Interval {
    int l;
    int r;
}

class Segment implements Cloneable {
    private static Modular modular = new Modular(1e9 + 7);

    private Segment left;
    private Segment right;
    private int val;
    private int sum;
    private boolean clear;

    private void clear() {
        clear = true;
        sum = 0;
        val = 0;
    }

    public void pushUp() {
        sum = modular.plus(left.sum, right.sum);
    }

    public void pushDown() {
        if (clear) {
            left.clear();
            right.clear();
            clear = false;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateClear(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            clear();
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateClear(ll, rr, l, m);
        right.updateClear(ll, rr, m + 1, r);
        pushUp();
    }

    public void updateVal(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            sum = val = modular.plus(val, x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateVal(ll, rr, l, m, x);
        right.updateVal(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = (l + r) >> 1;
        return modular.plus(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(val).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
