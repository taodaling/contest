package template;

/**
 * Created by dalt on 2018/5/25.
 */
public class Mathematics {

    public static int ceilPowerOf2(int x) {
        return 32 - Integer.numberOfLeadingZeros(x - 1);
    }

    public static int floorPowerOf2(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    public static long modmul(long a, long b, long mod) {
        return b == 0 ? 0 : ((modmul(a, b >> 1, mod) << 1) % mod + a * (b & 1)) % mod;
    }

    /**
     * Get the greatest common divisor of a and b
     */
    public static int gcd(int a, int b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private static int gcd0(int a, int b) {
        return b == 0 ? a : gcd0(b, a % b);
    }

    public static int extgcd(int a, int b, int[] coe) {
        if (a >= b) {
            return extgcd0(a, b, coe);
        } else {
            int g = extgcd0(b, a, coe);
            int tmp = coe[0];
            coe[0] = coe[1];
            coe[1] = tmp;
            return g;
        }
    }

    private static int extgcd0(int a, int b, int[] coe) {
        if (b == 0) {
            coe[0] = 1;
            coe[1] = 0;
            return a;
        }
        int g = extgcd0(b, a % b, coe);
        int n = coe[0];
        int m = coe[1];
        coe[0] = m;
        coe[1] = n - m * (a / b);
        return g;
    }

    /**
     * Get the greatest common divisor of a and b
     */
    public static long gcd(long a, long b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private static long gcd0(long a, long b) {
        return b == 0 ? a : gcd0(b, a % b);
    }

    public static long extgcd(long a, long b, long[] coe) {
        if (a >= b) {
            return extgcd0(a, b, coe);
        } else {
            long g = extgcd0(b, a, coe);
            long tmp = coe[0];
            coe[0] = coe[1];
            coe[1] = tmp;
            return g;
        }
    }

    private static long extgcd0(long a, long b, long[] coe) {
        if (b == 0) {
            coe[0] = 1;
            coe[1] = 0;
            return a;
        }
        long g = extgcd0(b, a % b, coe);
        long n = coe[0];
        long m = coe[1];
        coe[0] = m;
        coe[1] = n - m * (a / b);
        return g;
    }

    /**
     * Get y where x * y = 1 (% mod)
     */
    public static int inverse(int x, int mod) {
        return pow(x, mod - 2, mod);
    }

    /**
     * Get x^n(% mod)
     */
    public static int pow(int x, int n, int mod) {
        int bit = 31 - Integer.numberOfLeadingZeros(n);
        long product = 1;
        for (; bit >= 0; bit--) {
            product = product * product % mod;
            if (((1 << bit) & n) != 0) {
                product = product * x % mod;
            }
        }
        return (int) product;
    }

    public static long longpow(long x, long n, long mod) {
        if (n == 0) {
            return 1;
        }
        long prod = longpow(x, n >> 1, mod);
        prod = modmul(prod, prod, mod);
        if ((n & 1) == 1) {
            prod = modmul(prod, x, mod);
        }
        return prod;
    }

    /**
     * Get x % mod
     */
    public static int mod(int x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    public static int mod(long x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return (int) x;
    }

    /**
     * Get n!/(n-m)!
     */
    public static long permute(int n, int m) {
        return m == 0 ? 1 : n * permute(n - 1, m - 1);
    }

    /**
     * Put all primes less or equal to limit into primes after offset
     */
    public static int eulerSieve(int limit, int[] primes, int offset) {
        boolean[] isComp = new boolean[limit + 1];
        int wpos = offset;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[wpos++] = i;
            }
            for (int j = offset, until = limit / i; j < wpos && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                isComp[pi] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
        return wpos - offset;
    }

    /**
     * Round x into integer
     */
    public static int intRound(double x) {
        if (x < 0) {
            return -(int) (-x + 0.5);
        }
        return (int) (x + 0.5);
    }

    /**
     * Round x into long
     */
    public static long longRound(double x) {
        if (x < 0) {
            return -(long) (-x + 0.5);
        }
        return (long) (x + 0.5);
    }
}