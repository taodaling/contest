package template.math;

import template.primitve.generated.datastructure.IntegerHashMap;

/**
 * Used to find k while x^k = y % p and p is a prime
 */
public class ModPrimeLog {
    Modular mod;
    Modular powMod;
    int x;
    IntegerHashMap map;
    int m;
    CachedPow pow;

    public ModPrimeLog(int x, Modular mod) {
        this.x = x;
        this.mod = mod;
        pow = new CachedPow(x, mod.getMod() - 1, mod);
        powMod = new Modular(mod.getMod() - 1);
        m = (int) Math.ceil(Math.sqrt(mod.getMod()));
        map = new IntegerHashMap(m, false);
        for (int i = m - 1; i >= 0; i--) {
            map.put(pow.pow(i), i);
        }
    }

    /**
     * Return log_x y, if there doesn't exist such number, -1 will be returned
     */
    public int log(int y) {
        int start = 0;
        int end = mod.getMod() - 1;
        while (start * m < end) {
            int inverse = pow.pow(powMod.valueOf(-start * m));
            int val = map.getOrDefault(mod.mul(inverse, y), -1);
            if (val >= 0) {
                return powMod.valueOf(val + start * m);
            }
            start++;
        }
        return -1;
    }
}
