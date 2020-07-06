package template.math;

public class LongModular2305843009213693951 implements ILongModular {
    private static long mod = 2305843009213693951L;
    private static final LongModular2305843009213693951 INSTANCE = new LongModular2305843009213693951();

    private LongModular2305843009213693951() {
    }

    public static final LongModular2305843009213693951 getInstance() {
        return INSTANCE;
    }

    @Override
    public long getMod() {
        return mod;
    }

    @Override
    public long plus(long a, long b) {
        return valueOf(a + b);
    }

    @Override
    public long subtract(long a, long b) {
        return valueOf(a - b);
    }

    @Override
    public long valueOf(long x) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }


    private static long mask = (1L << 32) - 1;
    @Override
    public long mul(long a, long b) {
        long l1 = DigitUtils.lowBit(a) & mask;
        long h1 = DigitUtils.highBit(a) & mask;
        long l2 = DigitUtils.lowBit(b) & mask;
        long h2 = DigitUtils.highBit(b) & mask;
        long l = l1 * l2;
        long m = l1 * h2 + l2 * h1;
        long h = h1 * h2;
        long ret = (l & mod) + (l >>> 61) + (h << 3) + (m >>> 29) + ((m << 35) >>> 3) + 1;
        ret = (ret & mod) + (ret >>> 61);
        ret = (ret & mod) + (ret >>> 61);
        return ret - 1;
    }
}
