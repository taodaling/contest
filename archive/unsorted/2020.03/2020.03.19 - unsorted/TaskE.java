package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.TreeSet;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n + 1];
        int[] invIndex = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            p[i] = in.readInt();
            invIndex[p[i]] = i;
        }
        int[] q = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            q[i] = in.readInt();
        }

        int[] ans = new int[n + 1];
        int cur = n;
        ans[1] = cur;
        Segment segment = new Segment(1, n);
        segment.exist(invIndex[cur], invIndex[cur], 1, n, -1);
        IntegerBIT bomb = new IntegerBIT(n);
        IntegerBIT people = new IntegerBIT(n);
        people.update(invIndex[cur], 1);
        for (int i = 1; i < n; i++) {
            int b = q[i];
            bomb.update(b, 1);
            segment.update(1, b, 1, n, 1);
            while (segment.queryL(1, n, 1, n) >= 0) {
                cur--;
                people.update(invIndex[cur], 1);
                int val = bomb.query(invIndex[cur], n) - people.query(invIndex[cur], n);
                segment.exist(invIndex[cur], invIndex[cur], 1, n, val);
                segment.update(1, invIndex[cur] - 1, 1, n, -1);
            }
            ans[i + 1] = cur;
        }

        for (int i = 1; i <= n; i++) {
            out.append(ans[i]).append(' ');
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int val = (int) 1e9;
    private int mod;

    public void exist(int val) {
        this.val = val;
    }

    public void pushUp() {
        val = Math.min(left.val, right.val);
    }

    public void modify(int x) {
        mod += x;
        val += x;
    }

    public void pushDown() {
        if (mod != 0) {
            left.modify(mod);
            right.modify(mod);
            mod = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
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

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }


    public void exist(int ll, int rr, int l, int r, int val) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            exist(val);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.exist(ll, rr, l, m, val);
        right.exist(ll, rr, m + 1, r, val);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (int) 1e9;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
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
