package template.math;

import java.util.Random;

/**
 * Test whether a number is primes
 */
public class MillerRabin {
    Modular modular;
    Power power;
    Random random = new Random();

    /**
     * Check whether n is a prime s times
     */
    public boolean mr(int n, int s) {
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
        modular = new Modular(n);
        power = new Power(modular);
        for (int i = 0; i < s; i++) {
            int x = random.nextInt(n - 2) + 2;
            if (!mr0(x, n, m)) {
                return false;
            }
        }
        return true;
    }

    private boolean mr0(int x, int n, int m) {
        return test(power.pow(x, m), m, n);
    }

    private boolean test(int y, int exp, int n) {
        int y2 = modular.mul(y, y);
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
