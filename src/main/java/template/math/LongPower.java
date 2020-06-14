package template.math;

public class LongPower {
    public ILongModular getModular() {
        return modular;
    }

    final ILongModular modular;
    static LongExtGCDObject extGCD = new LongExtGCDObject();


    public LongPower(ILongModular modular) {
        this.modular = modular;
    }

    public long pow(long x, long n) {
        if (n == 0) {
            return 1;
        }
        long r = pow(x, n >> 1);
        r = modular.mul(r, r);
        if ((n & 1) == 1) {
            r = modular.mul(r, x);
        }
        return r;
    }

    public long inverseByFermat(long x) {
        return pow(x, modular.getMod() - 2);
    }

    public long inverseExtGCD(long x) {
        if (extGCD.extgcd(x, modular.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        return modular.valueOf(extGCD.getX());
    }

    public long inverse(long x) {
        return inverseExtGCD(x);
    }
}
