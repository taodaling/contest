package template.math;

import java.util.Random;

/**
 * Test whether a number is primes
 */
public class LongMillerRabin {
    public static void main(String[] args){
        System.out.println(LongMillerRabin.mr(1000003, 50));
    }

    static ILongModular modular;
    static LongPower power;
    static Random random = new Random();

    /**
     * Check whether n is a prime s times
     */
    public static boolean mr(long n, int s) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        long m = n - 1;
        while (m % 2 == 0) {
            m /= 2;
        }
        modular = ILongModular.getInstance(n);
        power = new LongPower(modular);
        for (int i = 0; i < s; i++) {
            long x = (long) (random.nextDouble() * (n - 2) + 2);
            if (!mr0(x, n, m)) {
                return false;
            }
        }
        return true;
    }


    private static boolean mr0(long x, long n, long m) {
        return test(power.pow(x, m), m, n);
    }

    private static boolean test(long y, long exp, long n) {
        long y2 = modular.mul(y, y);
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
