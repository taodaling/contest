package template.math;

import template.primitve.generated.datastructure.IntegerHashMap;

public class RelativePrimeModLog {
    Modular mod;
    Modular powMod;
    int x;
    int phi;
    IntegerHashMap map;
    int m;
    int invM;
    private static ExtGCD extGCD = new ExtGCD();

    public RelativePrimeModLog(int x, Modular mod) {
        this.x = x;
        this.mod = mod;
        phi = mod.getMod();
        powMod = new Modular(phi);
        if (extGCD.extgcd(x, mod.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        m = (int) Math.ceil(Math.sqrt(phi));
        map = new IntegerHashMap(m, false);

        int inv = mod.valueOf(extGCD.getX());
        invM = new Power(mod).pow(inv, m);

        int prod = mod.valueOf(1);
        for (int i = 0; i < m; i++) {
            map.putIfNotExist(prod, i);
            prod = mod.mul(prod, x);
        }
    }

    /**
     * return log_x y
     */
    public int log(int y) {
        y = mod.valueOf(y);
        int start = y;
        for (int i = 0; i * m < phi; start = mod.mul(start, invM), i++) {
            int val = map.getOrDefault(start, -1);
            if (val >= 0) {
                return powMod.valueOf(val + i * m);
            }
        }
        return -1;
    }
}