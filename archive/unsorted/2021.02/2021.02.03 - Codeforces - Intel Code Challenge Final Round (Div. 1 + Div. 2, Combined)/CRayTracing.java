package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExtGCD;
import template.math.GCDs;
import template.math.LongExtGCDObject;
import template.utils.Debug;

public class CRayTracing {
    /**
     * find min k that x + kd >= 0
     */
    public long addReq(long x, long d) {
        return DigitUtils.ceilDiv(-x, d);
    }

    long inf = (long) 2e18;

    public long best(long n, long m, long x, long y) {
        long[] ab = new long[2];
        long g = ExtGCD.extGCD(n, m, ab);
        if ((y - x) % g != 0) {
            return inf;
        }
        ab[1] = -ab[1];
        ab[0] *= (y - x) / g;
        ab[1] *= (y - x) / g;
        long addReq = Math.max(addReq(ab[0], m / g), addReq(ab[1], n / g));
        ab[0] += m / g * addReq;
        ab[1] += n / g * addReq;
        assert ab[0] >= 0 && ab[1] >= 0;
        return ab[0] * n + x;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.ri();
        long m = in.ri();
        long endTime = best(n, m, n, m);
        debug.debug("endTime", endTime);
        int k = in.ri();
        for (int i = 0; i < k; i++) {
            long x = in.ri();
            long y = in.ri();
            long ans = inf;
            for (int dx = 0; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    long nx = x + dx * (n - x) * 2;
                    long ny = y + dy * (m - y) * 2;
                    long cand = best(n * 2, m * 2, nx, ny);
                    ans = Math.min(ans, cand);
                }
            }
            if (ans >= endTime) {
                out.println(-1);
            } else {
                out.println(ans);
            }
        }
    }
}
