package template.math;

import template.primitve.generated.IntegerHashMap;

class RelativePrimeModLog {
    Modular mod;
    Modular powMod;
    int x;
    int invX;
    int phi;
    IntegerHashMap map;
    int m;
    CachedPow pow;
    private static ExtGCD extGCD = new ExtGCD();

    public RelativePrimeModLog(int x, Modular mod) {
        this.x = x;
        this.mod = mod;
        phi = mod.getMod();
        powMod = new Modular(phi);
        if (extGCD.extgcd(x, mod.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        invX = mod.valueOf(extGCD.getX());
        pow = new CachedPow(invX, mod);

        m = (int) Math.ceil(Math.sqrt(phi));
        map = new IntegerHashMap(m, false);

        int prod = mod.valueOf(1);
        for (int i = 0; i < m; i++) {
            map.putIfNotExist(prod, i);
            prod = mod.mul(prod, x);
        }
    }

    public int log(int y) {
        y = mod.valueOf(y);
        int start = 0;
        while (start * m < phi) {
            int inverse = pow.pow(powMod.valueOf(start * m));
            int val = map.getOrDefault(mod.mul(inverse, y), -1);
            if (val >= 0) {
                return powMod.valueOf(val + start * m);
            }
            start++;
        }
        return -1;
    }

}