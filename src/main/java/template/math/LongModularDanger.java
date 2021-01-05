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
}
