package template.math;

import template.primitve.generated.datastructure.IntegerHashMap;

public class RelativePrimeModLog {
    IntegerHashMap map;
    int modVal;
    int m;
    int invM;
    private static IntExtGCDObject extGCD = new IntExtGCDObject();
    Modular mod;
    int phi;

    public RelativePrimeModLog(int x, Modular mod) {
        this.mod = mod;
        modVal = mod.getMod();
        phi = CachedEulerFunction.get(modVal);
        if (extGCD.extgcd(x, mod.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        m = (int) Math.ceil(Math.sqrt(modVal));
        map = new IntegerHashMap(m, false);

        int inv = mod.valueOf(extGCD.getX());
        invM = new Power(mod).pow(inv, m);

        long prod = mod.valueOf(1);
        for (int i = 0; i < m; i++) {
            map.putIfNotExist((int) prod, i);
            prod = prod * x % modVal;
        }
    }

    /**
     * <p>return log_x y</p>
     * <p>O(p^0.5)</p>
     */
    public int log(int y) {
        y = mod.valueOf(y);
        long start = y;
        for (int i = 0; i * m < modVal; start = start * invM % modVal, i++) {
            int val = map.getOrDefault((int) start, -1);
            if (val >= 0) {
                return (int) (((long) val + i * m) % phi);
            }
        }
        return -1;
    }
}