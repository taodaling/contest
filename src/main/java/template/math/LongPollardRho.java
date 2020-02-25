package template.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LongPollardRho {
    public static void main(String[] args) {
        System.out.println(new LongPollardRho().findAllFactors(100000000000123503L));
    }

    LongMillerRabin mr = new LongMillerRabin();
    Random random = new Random();

    /**
     * Find a factor of n, if n is returned, it means n is 1 or a prime
     */
    public long findFactor(long n) {
        if (mr.mr(n, 5)) {
            return n;
        }

        while (true) {
            long f = rho(n);
            if (f != n) {
                return f;
            }
        }
    }

    public long findPrimeFactor(long n) {
        long ans = findFactor(n);
        return ans == n ? ans : findPrimeFactor(ans);
    }

    /**
     * Find the representation of n=p1^c1 * p2^c2 * ... * pm ^ cm. <br>
     * The returned map contained such entries: pi -> pi^ci
     */
    public Map<Long, Long> findAllFactors(long n) {
        Map<Long, Long> map = new HashMap();
        findAllFactors(map, n);
        return map;
    }

    private void findAllFactors(Map<Long, Long> map, long n) {
        if (n == 1) {
            return;
        }
        long f = findFactor(n);
        if (f == n) {
            Long value = map.get(f);
            if (value == null) {
                value = 1L;
            }
            map.put(f, value * f);
            return;
        }
        findAllFactors(map, f);
        findAllFactors(map, n / f);
    }

    private long rho(long n) {
        if (n % 2 == 0) {
            return 2;
        }
        if (n % 3 == 0) {
            return 3;
        }
        ILongModular modular = ILongModular.getInstance(n);
        long x = 0, y = x, t, q = 1, c = (long) (random.nextDouble() * (n - 1)) + 1;
        for (int k = 2; ; k <<= 1, y = x, q = 1) {
            for (int i = 1; i <= k; ++i) {
                x = modular.plus(modular.mul(x, x), c);
                q = modular.mul(q, Math.abs(x - y));
                if ((i & 127) == 0) {
                    t = GCDs.gcd(q, n);
                    if (t > 1) {
                        return t;
                    }
                }
            }
            if ((t = GCDs.gcd(q, n)) > 1) {
                return t;
            }
        }
    }
}
