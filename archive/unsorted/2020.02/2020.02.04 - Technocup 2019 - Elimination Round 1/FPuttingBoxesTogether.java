package contest;

import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;

import java.util.function.IntUnaryOperator;

public class FPuttingBoxesTogether {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] as = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            as[i] = in.readInt();
        }

        int[] ws = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ws[i] = in.readInt();
        }
        ModBIT bit = new ModBIT(n, mod);
        ModBIT indexBIT = new ModBIT(n, mod);
        Segment segment = new Segment(1, n, i -> ws[i]);
        for (int i = 1; i <= n; i++) {
            bit.update(i, mod.mul(as[i], ws[i]));
            indexBIT.update(i, mod.mul(i, ws[i]));
        }

        for (int i = 0; i < q; i++) {
            int x = in.readInt();
            int y = in.readInt();
            if (x < 0) {
                int id = -x;
                int nw = y;
                segment.update(id, id, 1, n, nw);
                bit.update(id, -bit.interval(id, id));
                bit.update(id, mod.mul(nw, as[id]));
                indexBIT.update(id, -indexBIT.interval(id, id));
                indexBIT.update(id, mod.mul(nw, id));
            } else {
                int l = x;
                int r = y;
                long leftWeight = segment.query(1, l - 1, 1, n);
                long wholeWeight = segment.query(l, r, 1, n);
                int center = segment.binarySearch(1, n, leftWeight + (wholeWeight + 1) / 2);

                int leftSum = mod.valueOf(segment.query(l, center - 1, 1, n));
                int rightSum = mod.valueOf(segment.query(center + 1, r, 1, n));
                int leftContrib1 = mod.subtract(mod.mul(as[center], leftSum),
                        bit.interval(l, center - 1));
                int rightContrib1 = mod.subtract(bit.interval(center + 1, r),
                        mod.mul(as[center], rightSum));
                int leftContrib2 = mod.subtract(indexBIT.interval(l, center - 1),
                        mod.mul(center, leftSum));
                int rightContrib2 = mod.subtract(mod.mul(center, rightSum),
                        indexBIT.interval(center + 1, r));

                int ans = mod.plus(leftContrib1, rightContrib1);
                ans = mod.plus(ans, leftContrib2);
                ans = mod.plus(ans, rightContrib2);
                out.println(ans);
            }
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long sum;

    private void setSum(long x) {
        sum = x;
    }

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntUnaryOperator operator) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, operator);
            right = new Segment(m + 1, r, operator);
            pushUp();
        } else {
            sum = operator.applyAsInt(l);
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
            setSum(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
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
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    public int binarySearch(int l, int r, long limit) {
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (left.sum >= limit) {
            return left.binarySearch(l, m, limit);
        }
        return right.binarySearch(m + 1, r, limit - left.sum);
    }
}