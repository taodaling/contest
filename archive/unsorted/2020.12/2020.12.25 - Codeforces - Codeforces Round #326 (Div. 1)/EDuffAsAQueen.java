package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.util.Arrays;

public class EDuffAsAQueen {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = new int[n];
        in.populate(a);
        LinearBasis query = new LinearBasis();
        Segment seg = new Segment(0, n - 1, i -> a[i]);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            if (t == 1) {
                int x = in.ri();
                seg.update(l, r, 0, n - 1, x);
            } else {
                query.clear();
                seg.query(l, r, 0, n - 1, query);
                int ans = query.xorNumberCount();
                out.println(ans);
            }
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int xor;
    private LinearBasis lb = new LinearBasis();
    private static LinearBasis buf = new LinearBasis();
    boolean odd;

    private void modify(int x) {
        xor ^= x;
        buf.copy(lb);
        lb.clear();

        if (odd && x != 0) {
            odd = false;
            lb.add(x, 1);
        }
        for (int i = 0; i < 30; i++) {
            if (buf.map[i] != 0) {
                if (lb.add(buf.map[i] ^ (x * buf.sign(i)), buf.sign(i)) == 1) {
                    odd = true;
                }
            }
        }
    }

    public void pushUp() {
        //xor = left.xor;
        odd = left.odd || right.odd;
        lb.copy(left.lb);
        for (int i = 0; i < 30; i++) {
            if (right.lb.map[i] != 0) {
                if (lb.add(right.lb.map[i], right.lb.sign(i)) % 2 == 1) {
                    odd = true;
                }
            }
        }
    }

    public void pushDown() {
        if (xor != 0) {
            left.modify(xor);
            right.modify(xor);
            xor = 0;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            if (lb.add(func.apply(l), 1) == 1) {
                odd = true;
            }
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, LinearBasis lb) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            for (int i = 0; i < 30; i++) {
                if (this.lb.map[i] != 0) {
                    lb.add(this.lb.map[i], this.lb.sign(i));
                }
            }
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, lb);
        right.query(ll, rr, m + 1, r, lb);
    }


}

class LinearBasis implements Cloneable {
    int[] map = new int[30];
    int set;
    int sign;

    public int size() {
        return Integer.bitCount(set);
    }

    public void clear() {
        set = 0;
        sign = 0;
        Arrays.fill(map, 0);
    }

    public void copy(LinearBasis model) {
        System.arraycopy(model.map, 0, map, 0, map.length);
        set = model.set;
        sign = model.sign;
    }

    /**
     * return the index of  added element ([0,64)), -1 means can't add val
     *
     * @param val
     * @return
     */
    public int add(int val, int s) {
        int step = s;
        for (int i = map.length - 1; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0 || map[i] == 0) {
                continue;
            }
            val ^= map[i];
            step ^= sign(i);
        }
        if (val != 0) {
            int log = Log2.floorLog(val);
            map[log] = val;
            set |= 1 << log;
            sign |= step << log;
            return 0;
        }
        return step & 1;
    }

    public int sign(int i) {
        return bitAt(sign, i);
    }

    private int bitAt(int val, int i) {
        return (val >>> i) & 1;
    }

    public int xorNumberCount() {
        return 1 << size();
    }
}
