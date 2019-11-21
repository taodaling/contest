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
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        modular = new Modular(n);
        power = new Power(modular);
        for (int i = 0; i < s; i++) {
            int x = random.nextInt(n - 2) + 2;
            if (!mr0(x, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean mr0(int x, int n) {
        int exp = n - 1;
        while (true) {
            int y = power.pow(x, exp);
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
