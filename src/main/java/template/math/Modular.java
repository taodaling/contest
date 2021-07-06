package template.math;

/**
 * There are some bugs
 */
@Deprecated
public class Modular {
    private final int mod;
    private final long h, l;
    private final long MAX = 1L << 62;
    private final int MASK = (1 << 31) - 1;

    public Modular(final int mod) {
        this.mod = mod;
        final long t = MAX / mod;
        h = t >>> 31;
        l = t & MASK;
    }

    public int getMod() {
        return mod;
    }

    public int valueOf(long x) {
        if (x < 0) {
            if (x < mod) {
                x = -reduce(-x);
            }
            if (x < 0) {
                x += mod;
            }
            return (int) x;
        }
        return reduce(x);
    }

    public int reduce(final long x) {
        final long xh = x >>> 31, xl = x & MASK;
        long z = xl * l;
        z = xl * h + xh * l + (z >>> 31);
        z = xh * h + (z >>> 31);
        final int ret = (int) (x - z * mod);
        int ans = ret >= mod ? ret - mod : ret;
        assert ans == x % mod;
        return ans;
    }

    public int mul(long a, long b) {
        return reduce(a * b);
    }

    public int pow2(long x) {
        return mul(x, x);
    }

    public int inv(int x) {
        return (int) DigitUtils.modInverse(x, mod);
    }

    public int mul(long a, long b, long c) {
        return reduce(reduce(a * b) * c);
    }

    public int mul(long a, long b, long c, long d) {
        return reduce(reduce(reduce(a * b) * c) * d);
    }

    public int add(int a, int b) {
        int ans = a + b;
        if (ans >= mod) {
            ans -= mod;
        } else if (ans < 0) {
            ans += mod;
        }
        return ans;
    }

    public int add(int a, int b, int c) {
        return add(add(a, b), c);
    }

    public int add(int a, int b, int c, int d) {
        return add(add(add(a, b), c), d);
    }

    public int sub(int a, int b) {
        return add(a, -b);
    }

    public int negate(int x) {
        return x == 0 ? 0 : mod - x;
    }
}