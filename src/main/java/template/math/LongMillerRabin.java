package template.math;

import java.util.Random;

/**
 * Test whether a number is primes
 */
public class LongMillerRabin {
    LongModular modular;
    LongPower power;
    Random random = new Random();

    /**
     * Check whether n is a prime s times
     */
    public boolean mr(long n, int s) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        modular = new LongModular(n);
        power = new LongPower(modular);
        for (int i = 0; i < s; i++) {
            long x = (long) (random.nextDouble() * (n - 2) + 2);
            if (!mr0(x, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean mr0(long x, long n) {
        long exp = n - 1;
        while (true) {
            long y = power.pow(x, exp);
            if (y != 1 && y != n - 1) {
                return false;
            }
            if (y != 1 || exp % 2 == 1) {
                break;
            }
            exp = exp / 2;
        }
        return true;
    }
}
