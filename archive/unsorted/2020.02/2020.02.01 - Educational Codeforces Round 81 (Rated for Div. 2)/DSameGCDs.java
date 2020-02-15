package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.primitve.generated.LongList;

public class DSameGCDs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long m = in.readLong();
        long g = GCDs.gcd(a, m);
        long ans = solve(a + m - 1, m, g);
        ans -= solve(a - 1, m, g);
        out.println(ans);
    }

    public long solve(long n, long x, long g) {
        long[] factors = Factorization.factorizeNumberPrime(x / g).toArray();
        return dfs(factors, 0, 1, 0, n, g);
    }

    public long dfs(long[] factors, int i, long d, int cnt, long n, long g) {
        if (i == factors.length) {
            long contri = n / g / d;
            if (cnt % 2 == 1) {
                contri = -contri;
            }
            return contri;
        }
        return dfs(factors, i + 1, d * factors[i], cnt + 1, n, g) +
                dfs(factors, i + 1, d, cnt, n, g);
    }
}
