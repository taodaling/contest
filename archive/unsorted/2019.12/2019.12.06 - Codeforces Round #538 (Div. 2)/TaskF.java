package contest;

import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.Modular;
import template.math.Power;

import java.util.Arrays;

public class TaskF {
    int[] allPrimes;
    int k = 62;
    Modular mod = new Modular(1e9 + 7);
    Segment sumSeg;
    int n;
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        EulerSieve sieve = new EulerSieve(300);
        allPrimes = new int[sieve.getPrimeCount()];
        for (int i = 0; i < allPrimes.length; i++) {
            allPrimes[i] = sieve.get(i);
        }
        k = allPrimes.length;
        sumSeg = new Segment(1, n);
        for (int i = 1; i <= n; i++) {
            mul(i, i, in.readInt());
        }

        char[] cmd = new char[100];
        for (int i = 0; i < q; i++) {
            in.readString(cmd, 0);
            if (cmd[0] == 'M') {
                mul(in.readInt(), in.readInt(), in.readInt());
            } else {
                int ans = query(in.readInt(), in.readInt());
                out.println(ans);
            }
        }
    }

    int[] buf = new int[62];

    private void prepareBuf() {
        Arrays.fill(buf, 0);
    }

    public void mul(int l, int r, int x) {
        prepareBuf();
        for (int i = 0; i < k; i++) {
            int y = x;
            while (y % allPrimes[i] == 0) {
                buf[i]++;
                y /= allPrimes[i];
            }
        }
        sumSeg.update(l, r, 1, n, buf);
    }

    public int query(int l, int r) {
        prepareBuf();
        sumSeg.query(l, r, 1, n, buf);
        int ans = 1;
        for (int i = 0; i < k; i++) {
            if (buf[i] == 0) {
                continue;
            }
            ans = mod.mul(ans, power.pow(allPrimes[i], buf[i] - 1));
            ans = mod.mul(ans, mod.subtract(allPrimes[i], 1));
        }
        return ans;
    }
}


class Segment implements Cloneable {
    private static final int K = 62;
    private static Modular mod = new Modular(1e9 + 7);
    private Segment left;
    private Segment right;
    private int size;
    private int[] sum = new int[K];
    private int[] dirty = new int[K];

    public void setDirty(int[] d) {
        for (int i = 0; i < K; i++) {
            if (d[i] == 0) {
                continue;
            }
            dirty[i] = mod.plus(dirty[i], d[i]);
            sum[i] = mod.plus(sum[i], mod.mul(size, d[i]));
        }
    }

    public void pushUp() {
        size = left.size + right.size;
        for (int i = 0; i < K; i++) {
            sum[i] = mod.plus(left.sum[i], right.sum[i]);
        }
    }

    public void pushDown() {
        left.setDirty(dirty);
        right.setDirty(dirty);
        Arrays.fill(dirty, 0);
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            size = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int[] d) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setDirty(d);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, d);
        right.update(ll, rr, m + 1, r, d);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, int[] ans) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            for (int i = 0; i < K; i++) {
                ans[i] = mod.plus(ans[i], sum[i]);
            }
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m, ans);
        right.query(ll, rr, m + 1, r, ans);
    }
}
