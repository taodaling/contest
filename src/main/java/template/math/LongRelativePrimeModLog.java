package template.math;

import template.primitve.generated.datastructure.LongHashMap;

public class LongRelativePrimeModLog {
    ILongModular mod;
    ILongModular powMod;
    long x;
    long phi;
    LongHashMap map;
    int m;
    LongCachedPow pow;
    private static ExtGCD extGCD = new ExtGCD();

    public LongCachedPow getPowX() {
        return pow;
    }

    public LongRelativePrimeModLog(long x, ILongModular mod) {
        this.x = x;
        this.mod = mod;
        phi = mod.getMod();
        powMod = mod.getModularForPowerComputation();
        if (extGCD.extgcd(x, mod.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        pow = new LongCachedPow(x, mod);

        m = (int) Math.ceil(Math.sqrt(phi));
        map = new LongHashMap(m, false);

        long prod = mod.valueOf(1);
        for (int i = 0; i < m; i++) {
            map.putIfNotExist(prod, i);
            prod = mod.mul(prod, x);
        }
    }

    public long log(long y) {
        y = mod.valueOf(y);
        long start = 0;
        while (start < phi) {
            long inverse = pow.inverse(start);
            long val = map.getOrDefault(mod.mul(inverse, y), -1);
            if (val >= 0) {
                return powMod.valueOf(val + start);
            }
            start += m;
        }
        return -1;
    }
}
