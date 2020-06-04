package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.utils.Debug;

import java.util.Arrays;

public class FLehaAndSecuritySystem {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        Segment seg = new Segment(1, n, i -> a[i]);
        int[] cast = new int[10];
        debug.debug("seg", seg);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int l = in.readInt();
                int r = in.readInt();
                int x = in.readInt();
                int y = in.readInt();
                Segment.asStandard(cast);
                cast[x] = y;
                seg.update(l, r, 1, n, cast);
                debug.debug("seg", seg);
            } else {
                int l = in.readInt();
                int r = in.readInt();
                long sum = seg.query(l, r, 1, n);
                out.println(sum);
            }
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static long[] buf = new long[10];
    long[] sum = new long[10];
    int[] dirty = new int[10];
    boolean tag = false;

    public static void asStandard(int[] x) {
        for (int i = 0; i < 10; i++) {
            x[i] = i;
        }
    }

    private void modify(int[] x) {
        Arrays.fill(buf, 0);
        for (int i = 0; i < 10; i++) {
            dirty[i] = x[dirty[i]];
            buf[x[i]] += sum[i];
        }
        for (int i = 0; i < 10; i++) {
            sum[i] = buf[i];
        }
        tag = true;
    }

    public void pushUp() {
        for (int i = 0; i < 10; i++) {
            sum[i] = left.sum[i] + right.sum[i];
        }
    }

    public void pushDown() {
        if (tag) {
            left.modify(dirty);
            right.modify(dirty);
            tag = false;
            asStandard(dirty);
        }
    }

    public Segment(int l, int r, IntToIntFunction func) {
        asStandard(dirty);
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            int val = func.apply(l);
            int base = 1;
            while (val != 0) {
                sum[val % 10] += base;
                base *= 10;
                val /= 10;
            }
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int[] x) {
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

    private static long parse(long[] sum) {
        long ans = 0;
        for (int i = 0; i < 10; i++) {
            ans += sum[i] * i;
        }
        return ans;
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return parse(sum);
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
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
            builder.append(parse(sum)).append(",");
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
