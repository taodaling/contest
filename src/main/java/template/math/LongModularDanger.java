package template.math;

public class LongModularDanger implements ILongModular {
    final long mod;

    @Override
    public long getMod() {
        return mod;
    }

    public LongModularDanger(long m) {
        this.mod = m;
    }

    public long mul(long a, long b) {
        long k = DigitUtils.round((double) a / mod * b);
        return DigitUtils.mod(a * b - k * mod, mod);
    }
}
