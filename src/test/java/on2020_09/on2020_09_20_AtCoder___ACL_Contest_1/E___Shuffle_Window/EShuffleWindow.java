package on2020_09.on2020_09_20_AtCoder___ACL_Contest_1.E___Shuffle_Window;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;

public class EShuffleWindow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] p = new int[n];
        in.populate(p);
        Segment segment = new Segment(1, n);
        int die = power.inverse(k);
        int alive = modular.subtract(1, die);
        int half = power.inverse(2);
        long exp = 0;
        long inverseCnt = 0;
        for (int i = 0; i < n; i++) {
            if (i >= k) {
                segment.update(1, n, 1, n, alive);
            }
            long pre = segment.query(1, p[i], 1, n);
            long post = segment.query(p[i], n, 1, n);
            exp += half * pre % mod;
            exp -= half * post % mod;

            int postSize = segment.querySize(p[i], n, 1, n);
            inverseCnt += postSize;

            segment.active(p[i], p[i], 1, n);
        }
        long ans = exp + inverseCnt;
        ans = modular.valueOf(ans);
        out.println(ans);
    }

    private static long mod = 998244353;
    private static Modular modular = new Modular(mod);
    private static Power power = new Power(modular);
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long mod = 998244353;
    private long sum = 0;
    private long mul = 1;
    private int size = 0;

    private void active() {
        size = 1;
        sum = 1;
    }

    private void modify(long x) {
        sum = sum * x % mod;
        mul = mul * x % mod;
    }

    public void pushUp() {
        sum = (left.sum + right.sum) % mod;
        size = left.size + right.size;
    }

    public void pushDown() {
        if (mul != 1) {
            left.modify(mul);
            right.modify(mul);
            mul = 1;
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

    public void update(int ll, int rr, int l, int r, long x) {
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

    public void active(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            active();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.active(ll, rr, l, m);
        right.active(ll, rr, m + 1, r);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return (left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r)) % mod;
    }

    public int querySize(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return size;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.querySize(ll, rr, l, m) +
                right.querySize(ll, rr, m + 1, r);
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
            builder.append("val").append(",");
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
