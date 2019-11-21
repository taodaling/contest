package template.math;

import template.math.Gcd;
import template.math.MillerRabin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Find all factors of a number
 */
public class PollardRho {
    MillerRabin mr = new MillerRabin();
    Gcd gcd = new Gcd();
    Random random = new Random();

    public int findFactor(int n) {
        if (mr.mr(n, 10)) {
            return n;
        }
        while (true) {
            int f = findFactor0(random.nextInt(n), random.nextInt(n), n);
            if (f != -1) {
                return f;
            }
        }
    }

    /**
     * Find all prime factor of n <br>
     * p1 => p1^c1 <br>
     * ... <br>
     * pk => pk^ck
     */
    public Map<Integer, Integer> findAllFactors(int n) {
        Map<Integer, Integer> map = new HashMap();
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

    private int findFactor0(int x, int c, int n) {
        int xi = x;
        int xj = x;
        int j = 2;
        int i = 1;
        while (i < n) {
            i++;
            xi = (int) ((long) xi * xi + c) % n;
            int g = gcd.gcd(n, Math.abs(xi - xj));
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
}
