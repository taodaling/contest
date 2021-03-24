package template.math;

import template.rand.RandomWrapper;

import java.util.*;

/**
 * Find all factors of a number
 */
public class PollardRho {

    public int findFactor(int n) {
        if (n == 1) {
            return n;
        }
        if (MillerRabin.mr(n, 10)) {
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
        if (n == 1) {
            return n;
        }
        int ans = findFactor(n);
        return ans == n ? ans : findPrimeFactor(ans);
    }

    /**
     * Find all prime factor of n <br>
     * p1 => p1^c1 <br>
     * ... <br>
     * pk => pk^ck
     */
    public Set<Integer> findAllFactors(int n) {
        if (n == 1) {
            return Collections.emptySet();
        }
        Set<Integer> set = new HashSet<>();
        findAllFactors(set, n);
        return set;
    }

    private void findAllFactors(Set<Integer> set, int n) {
        int f = findFactor(n);
        if (f == n) {
            set.add(f);
            return;
        }
        findAllFactors(set, f);
        findAllFactors(set, n / f);
    }

    private int rho(int n) {
        if (n == 1) {
            return 1;
        }
        if (n % 2 == 0) {
            return 2;
        }
        if (n % 3 == 0) {
            return 3;
        }
        int x = 0, y = x, t, q = 1, c = RandomWrapper.INSTANCE.nextInt(n - 1) + 1;
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
