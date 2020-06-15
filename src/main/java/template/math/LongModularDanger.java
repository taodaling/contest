package template.math;

public class LongModularDanger implements ILongModular {
    final long m;

    @Override
    public long getMod() {
        return m;
    }

    public LongModularDanger(long m) {
        this.m = m;
    }

    public long mul(long a, long b) {
        return DigitUtils.modmul(a, b, m);
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
