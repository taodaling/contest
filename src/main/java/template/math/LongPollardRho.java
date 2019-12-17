package template.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LongPollardRho {
    LongMillerRabin mr = new LongMillerRabin();
    ILongModular modular;
    Random random = new Random();


    /**
     * Find a factor of n, if n is returned, it means n is 1 or a prime
     */
    public long findFactor(long n) {
        if (mr.mr(n, 3)) {
            return n;
        }
        modular = ILongModular.getInstance(n);
        while (true) {
            long f = findFactor0((long) (random.nextDouble() * n), (long) (random.nextDouble() * n), n);
            if (f != -1) {
                return f;
            }
        }
    }

    private long findFactor0(long x, long c, long n) {
        long xi = x;
        long xj = x;
        int j = 2;
        int i = 1;
        while (i < n) {
            i++;
            xi = modular.plus(modular.mul(xi, xi), c);
            long g = GCDs.gcd(n, Math.abs(xi - xj));
            if (g != 1 && g != n) {
                return g;
            }
            if (i == j) {
                j = j << 1;
                xj = xi;
            }
        }
        return -1;
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
}
