package template.math;

import java.math.BigInteger;

public class BigLongModular implements ILongModular {
    public BigLongModular(long mod) {
        this.mod = mod;
        this.bigMod = BigInteger.valueOf(mod);
    }

    long mod;
    BigInteger bigMod;

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

    @Override
    public long mul(long a, long b) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(bigMod).longValue();
    }
}
