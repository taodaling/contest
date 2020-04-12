package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.LCMs;
import template.math.Modular;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.primitve.generated.datastructure.LongList;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class EDivisorPaths {

    Debug debug = new Debug(true);

    /**
     * log_x y
     */
    public int log(long x, long y) {
        int ans = 0;
        while (y % x == 0) {
            y /= x;
            ans++;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long D = in.readLong();
        long[] factors = Factorization.factorizeNumber(D).toArray();
        int m = factors.length;
        Randomized.shuffle(factors);
        Arrays.sort(factors);
        LongHashMap map = new LongHashMap(m, false);
        for (int i = 0; i < m; i++) {
            map.put(factors[i], i);
        }
        long[] primes = Factorization.factorizeNumberPrime(D).toArray();
        long[] total = new long[m];
        for (int i = 0; i < m; i++) {
            total[i] = 1;
            for (long p : primes) {
                total[i] *= log(p, factors[i]) + 1;
            }
        }

        Modular mod = new Modular(998244353);
        long[] dp = new long[m];
        int[] way = new int[m];
        SequenceUtils.deepFill(dp, (long) 1e18);
        dp[0] = 0;
        way[0] = 1;
        for (int i = 1; i < m; i++) {
            for (long p : primes) {
                if (factors[i] % p != 0) {
                    continue;
                }
                int j = (int) map.get(factors[i] / p);
                long req = dp[j] + total[i] / (log(p, factors[i]) + 1);
                if (req < dp[i]) {
                    dp[i] = req;
                    way[i] = 0;
                }
                if (req == dp[i]) {
                    way[i] = mod.plus(way[i], way[j]);
                }
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            long u = in.readLong();
            long v = in.readLong();
            long lcm = LCMs.lcm(u, v);
            int way1 = way[(int) map.get(lcm / u)];
            int way2 = way[(int) map.get(lcm / v)];
            long ans = mod.mul(way1, way2);
            out.println(ans);
        }
    }
}
