package template.math;

import template.rand.RandomWrapper;

import java.util.Random;

/**
 * Test whether a number is primes
 */
public class MillerRabin {
    static int mod;
    static Power power;

    /**
     * Check whether n is a prime s times, O(slog n)
     */
    public static boolean mr(int n, int s) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        int m = n - 1;
        while (m % 2 == 0) {
            m /= 2;
        }
        mod = n;
        power = new Power(mod);
        for (int i = 0; i < s; i++) {
            int x = RandomWrapper.INSTANCE.nextInt(n - 2) + 2;
            if (!mr0(x, n, m)) {
                return false;
            }
        }
        return true;
    }

    private static boolean mr0(int x, int n, int m) {
        return test(power.pow(x, m), m, n);
    }

    private static boolean test(int y, int exp, int n) {
        int y2 = (int) ((long) y * y % mod);
        if (!(exp == n - 1 || test(y2, exp * 2, n))) {
            return false;
        }
        if (exp != n - 1 && y2 != 1) {
            return true;
        }
        if (y != 1 && y != n - 1) {
            return false;
        }
        return true;
    }
}
