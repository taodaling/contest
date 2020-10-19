package template.math;

/**
 * Modular operation for long version
 */
public class LongModular implements ILongModular {
    final long m;

    @Override
    public long getMod() {
        return m;
    }

    public LongModular(long m) {
        this.m = m;
    }


    public long mul(long a, long b) {
        if (b == 0) {
            return 0;
        }
        long ans = mul(a, b >> 1) << 1;
        if (ans >= m) {
            ans -= m;
        }
        ans += a * (b & 1);
        if (ans >= m) {
            ans -= m;
        }
        return ans;
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
