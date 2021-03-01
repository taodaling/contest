package template.math;

public class SmallLongModular implements ILongModular {
    long mod;

    public SmallLongModular(long mod) {
        this.mod = mod;
    }

    @Override
    public long getMod() {
        return mod;
    }

    @Override
    public long mul(long a, long b) {
        return a * b % mod;
    }
}
