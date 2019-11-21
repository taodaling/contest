package template.math;

/**
 * Extend gcd
 */
public class ExtGCD {
    private long x;
    private long y;
    private long g;

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    /**
     * Get g = Gcd(a, b) and find a way to set x and y to check ax+by=g
     */
    public long extgcd(long a, long b) {
        if (a >= b) {
            g = extgcd0(a, b);
        } else {
            g = extgcd0(b, a);
            long tmp = x;
            x = y;
            y = tmp;
        }
        return g;
    }


    private long extgcd0(long a, long b) {
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        long g = extgcd0(b, a % b);
        long n = x;
        long m = y;
        x = m;
        y = n - m * (a / b);
        return g;
    }
}
