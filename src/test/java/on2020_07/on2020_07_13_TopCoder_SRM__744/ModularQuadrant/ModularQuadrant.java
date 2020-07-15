package on2020_07.on2020_07_13_TopCoder_SRM__744.ModularQuadrant;



import template.math.DigitUtils;
import template.utils.Debug;

public class ModularQuadrant {
    public long sum(int r1, int r2, int c1, int c2) {
        long ans = count(r2, c2) - count(r2, c1 - 1) - count(r1 - 1, c2) + count(r1 - 1, c1 - 1);
        return ans;
    }

    public long[] split(long x) {
        long[] ans = new long[3];
        for (int i = 0; i < 3; i++) {
            ans[i] = x / 3;
        }
        for (int i = 0; i < x % 3; i++) {
            ans[i]++;
        }
        return ans;
    }

    public long sum(long n) {
        if (n < 0) {
            return 0;
        }
        long[] cnts = split(n);
        long ans = 0;
        for (int i = 0; i < 3; i++) {
            ans += cnts[i] * i;
        }
        return ans;
    }

    public long sum(long l, long r) {
        if (l > r) {
            return 0;
        }
        return sum(r + 1) - sum(l);
    }

//    Debug debug = new Debug(true);

    public long partialSum(long n) {
        //0 + 1 + ... + n
        return n * (n + 1) / 2;
    }

    public long count(long n, long m) {
        if (n < 0 || m < 0) {
            return 0;
        }
        if (n < m) {
            long tmp = n;
            n = m;
            m = tmp;
        }
        long start = (m + 1) / 3 * 3;
        long ans = partialSum(start - 1) + start / 3 * 2;
        for (long i = start; i <= m; i++) {
            ans += (i % 3) * i;
        }
        ans = ans * 2 + sum(m + 1);
        ans += (m + 1) * sum(m + 1, n);

//        debug.debug("n", n);
//        debug.debug("m", m);
//        debug.debug("ans", ans);
        return ans;
    }
}
