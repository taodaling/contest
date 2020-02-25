package template.math;

public class DigitUtils {

    private DigitUtils() {
    }

    public static double normalizeTo(double x, double y, double prec) {
        if (Math.abs(x - y) < prec) {
            return y;
        }
        return x;
    }

    public static int roundToInt(double x) {
        return (int) round(x);
    }

    public static long round(double x) {
        if (x >= 0) {
            return (long) (x + 0.5);
        } else {
            return (long) (x - 0.5);
        }
    }

    public static double normalizeToZero(double x, double prec) {
        return normalizeTo(x, 0, prec);
    }

    /**
     * Return minimum integer x satisfy a / b < x
     */
    public static long minimumIntegerGreaterThanDiv(long a, long b) {
        return floorDiv(a, b) + 1;
    }

    /**
     * Return minimum integer x satisfy a / b < x
     */
    public static long maximumIntegerLessThanDiv(long a, long b) {
        return ceilDiv(a, b) - 1;
    }

    /**
     * return floor(a / b) while b is positive
     */
    public static long floorDiv(long a, long b) {
        return a < 0 ? -ceilDiv(-a, b) : a / b;
    }

    /**
     * return floor(a / b) while b is positive
     */
    public static int floorDiv(int a, int b) {
        return a < 0 ? -ceilDiv(-a, b) : a / b;
    }

    /**
     * return ceil(a / b) while b is positive
     */
    public static long ceilDiv(long a, long b) {
        if (a < 0) {
            return -floorDiv(-a, b);
        }
        long c = a / b;
        if (c * b < a) {
            return c + 1;
        }
        return c;
    }

    public static boolean isMultiplicationOverflow(long a, long b, long limit) {
        if (limit < 0) {
            limit = -limit;
        }
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        if (a == 0 || b == 0) {
            return false;
        }
        //a * b > limit => a > limit / b
        return a > limit / b;
    }

    public static boolean isPlusOverflow(long a, long b) {
        if (Long.signum(a) != Long.signum(b)) {
            return false;
        }
        if (a < 0) {
            return a + b > 0;
        } else {
            return a + b < 0;
        }
    }

    public static long mul(long a, long b, long limit, long overflowVal) {
        return isMultiplicationOverflow(a, b, limit) ? overflowVal : a * b;
    }

    public static long mul(long a, long b, long overflowVal) {
        return mul(a, b, Long.MAX_VALUE, overflowVal);
    }

    /**
     * return ceil(a / b) while b is positive
     */
    public static int ceilDiv(int a, int b) {
        if (a < 0) {
            return -floorDiv(-a, b);
        }
        int c = a / b;
        if (c * b < a) {
            return c + 1;
        }
        return c;
    }

    private static long mask32 = (1L << 32) - 1;

    public static long asLong(int high, int low) {
        return ((((long) high)) << 32) | (((long) low) & mask32);
    }

    public static int highBit(long x) {
        return (int) (x >> 32);
    }

    public static int lowBit(long x) {
        return (int) x;
    }

    public static boolean isOdd(int x) {
        return (x & 1) == 1;
    }

    public static boolean isOdd(long x) {
        return (x & 1) == 1;
    }

    public static boolean isEven(int x) {
        return (x & 1) == 0;
    }

    public static boolean isEven(long x) {
        return (x & 1) == 0;
    }

    public static int mod(long x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return (int) x;
    }

    public static int mod(int x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    public static long mod(long x, long mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    /**
     * If mod <= 10^16(2^52), this method is pretty safe
     */
    public static long mulMod(long a, long b, long mod) {
        long k = DigitUtils.round((double) a / mod * b);
        return DigitUtils.mod(a * b - k * mod, mod);
    }

    public static int limitPow(int x, long n, int limit) {
        if (n == 0) {
            return Math.min(1, limit);
        }
        int ans = limitPow(x, n / 2, limit);
        ans = (int) Math.min((long) ans * ans, limit);
        if (n % 2 == 1) {
            ans = (int) Math.min((long) ans * x, limit);
        }
        return ans;
    }

    public static int modPow(int x, long n, int m) {
        if (n == 0) {
            return DigitUtils.mod(1, m);
        }
        int ans = modPow(x, n / 2, m);
        ans = DigitUtils.mod((long) ans * ans, m);
        if (n % 2 == 1) {
            ans = DigitUtils.mod((long) ans * x, m);
        }
        return ans;
    }
}
