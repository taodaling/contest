package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

import java.util.Arrays;
import java.util.Map;

public class TaskE {
    int[] primes;
    int[] primeExps;
    int k;
    Segment segs;
    int n;
    ExtGCD gcd = new ExtGCD();
    int[] buf1;
    int[] buf2;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        Map<Integer, Integer> factors = new PollardRho()
                .findAllFactors(m);

        IntegerList primeList = new IntegerList();
        IntegerList primeExpList = new IntegerList();
        for (Map.Entry<Integer, Integer> e : factors.entrySet()) {
            primeList.add(e.getKey());
            primeExpList.add(e.getValue());
        }

        primes = primeList.toArray();
        primeExps = primeExpList.toArray();
        k = primes.length;

        Segment.allPrimes = primes;
        Segment.modulars = new Modular[k];
        Segment.powers = new CachedPow[k];
        Segment.k = k;
        for (int i = 0; i < k; i++) {
            Segment.modulars[i] = new Modular(primeExps[i]);
            Segment.powers[i] = new CachedPow(primes[i], 10000000, Segment.modulars[i]);
        }

        segs = new Segment(1, n);
        buf1 = new int[k];
        buf2 = new int[k];

        for (int i = 1; i <= n; i++) {
            int a = in.readInt();
            mul(i, i, a);
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                mul(in.readInt(), in.readInt(), in.readInt());
            } else if (t == 2) {
                divide(in.readInt(), in.readInt());
            } else {
                int ans = query(in.readInt(), in.readInt());
                out.println(ans);
            }
        }
    }

    public void mul(int l, int r, int x) {
        for (int i = 0; i < k; i++) {
            int y = x;
            int p = 0;
            while (y % primes[i] == 0) {
                y /= primes[i];
                p++;
            }
            buf1[i] = y;
            buf2[i] = p;
        }
        segs.update(l, r, 1, n, buf1, buf2);
    }

    public void divide(int pos, int x) {
        for (int i = 0; i < k; i++) {
            int y = x;
            int p = 0;
            while (y % primes[i] == 0) {
                y /= primes[i];
                p++;
            }
            gcd.extgcd(y, primeExps[i]);
            int invY = DigitUtils.mod(gcd.getX(), primeExps[i]);
            buf1[i] = invY;
            buf2[i] = -p;
        }
        segs.update(pos, pos, 1, n, buf1, buf2);
    }

    public int query(int l, int r) {
        IntExtCRT crt = new IntExtCRT();
        Arrays.fill(buf1, 0);
        segs.query(l, r, 1, n, buf1);
        for (int i = 0; i < k; i++) {
            crt.add(buf1[i], primeExps[i]);
        }
        return crt.getValue();
    }

}

class Segment implements Cloneable {
    public static int k;
    public static int[] allPrimes;
    public static Modular[] modulars;
    public static CachedPow[] powers;
    private Segment left;
    private Segment right;
    private int[] sum = new int[k];
    private int[] val = new int[k];
    private int[] pow = new int[k];

    private void mod(int[] mul, int[] p) {
        for (int i = 0; i < k; i++) {
            if (p[i] >= 0) {
                sum[i] = modulars[i].mul(mul[i], sum[i]);
                sum[i] = modulars[i].mul(sum[i], powers[i].pow(p[i]));
                val[i] = modulars[i].mul(val[i], mul[i]);
                pow[i] += p[i];
            } else {
                sum[i] = val[i] = modulars[i].mul(val[i], mul[i]);
                pow[i] += p[i];
                sum[i] = modulars[i].mul(sum[i], powers[i].pow(pow[i]));
            }
        }
    }

    public void pushUp() {
        for (int i = 0; i < k; i++) {
            sum[i] = modulars[i].plus(left.sum[i], right.sum[i]);
        }
    }

    public void pushDown() {
        left.mod(val, pow);
        right.mod(val, pow);
        Arrays.fill(val, 1);
        Arrays.fill(pow, 0);

    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            Arrays.fill(val, 1);
            pushUp();
        } else {
            Arrays.fill(val, 1);
            Arrays.fill(sum, 1);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int[] mul, int[] p) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            mod(mul, p);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, mul, p);
        right.update(ll, rr, m + 1, r, mul, p);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, int[] ans) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            for (int i = 0; i < k; i++) {
                ans[i] = modulars[i].plus(sum[i], ans[i]);
            }
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m, ans);
        right.query(ll, rr, m + 1, r, ans);
    }

}