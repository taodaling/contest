package template.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Find all factors of a number
 */
public class PollardRho {
    MillerRabin mr = new MillerRabin();
    Random random = new Random(1);

    public int findFactor(int n) {
        if (mr.mr(n, 5)) {
            return n;
        }
        while (true) {
            int f = rho(n);
            if (f != n) {
                return f;
            }
        }
    }

    public int findPrimeFactor(int n) {
        int ans = findFactor(n);
        return ans == n ? ans : findPrimeFactor(ans);
    }

    /**
     * Find all prime factor of n <br>
     * p1 => p1^c1 <br>
     * ... <br>
     * pk => pk^ck
     */
    public Map<Integer, Integer> findAllFactors(int n) {
        Map<Integer, Integer> map = new HashMap<>();
        findAllFactors(map, n);
        return map;
    }

    private void findAllFactors(Map<Integer, Integer> map, int n) {
        if (n == 1) {
            return;
        }
        int f = findFactor(n);
        if (f == n) {
            Integer value = map.get(f);
            if (value == null) {
                value = 1;
            }
            map.put(f, value * f);
            return;
        }
        findAllFactors(map, f);
        findAllFactors(map, n / f);
    }

    private int rho(int n) {
        if (n % 2 == 0) {
            return 2;
        }
        if (n % 3 == 0) {
            return 3;
        }
        int x = 0, y = x, t, q = 1, c = random.nextInt(n - 1) + 1;
        for (int k = 2; ; k <<= 1, y = x, q = 1) {
            for (int i = 1; i <= k; ++i) {
                x = DigitUtils.mod((long) x * x + c, n);
                q = DigitUtils.mod((long) q * Math.abs(x - y), n);
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
