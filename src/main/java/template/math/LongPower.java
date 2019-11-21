package template.math;

import template.math.LongModular;

public class LongPower {
    public LongModular getModular() {
        return modular;
    }

    final LongModular modular;

    public LongPower(LongModular modular) {
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

    public long inverse(long x) {
        return pow(x, modular.m - 2);
    }
}
