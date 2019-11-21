package template.math;

/**
 * Modular operation for long version
 */
public class LongModular {
    final long m;

    public LongModular(long m) {
        this.m = m;
    }

    public long mul(long a, long b) {
        return b == 0 ? 0 : ((mul(a, b >> 1) << 1) % m + a * (b & 1)) % m;
    }

    public long plus(long a, long b) {
        return valueOf(a + b);
    }

    public long subtract(long a, long b) {
        return valueOf(a - b);
    }

    public long valueOf(long a) {
        a %= m;
        if (a < 0) {
            a += m;
        }
        return a;
    }
}
