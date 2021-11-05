package template.math;

import template.primitve.generated.datastructure.IntegerHashMap;

public class CoprimeModLog {
    private static IntExtGCDObject extGCD = new IntExtGCDObject();
    IntegerHashMap map;
    int mod;
    int m;
    int invM;
    int phi;

    public CoprimeModLog(int x, int mod) {
        this.mod = mod;
        phi = CachedEulerFunction.get(mod);
        if (extGCD.extgcd(x, mod) != 1) {
            throw new IllegalArgumentException();
        }
        m = (int) Math.ceil(Math.sqrt(mod));
        map = new IntegerHashMap(m, false);

        int inv = DigitUtils.mod(extGCD.getX(), mod);
        invM = new Power(mod).pow(inv, m);

        long prod = DigitUtils.mod(1, mod);
        for (int i = 0; i < m; i++) {
            map.put((int) prod, i);
            prod = prod * x % mod;
        }
    }

    public int log(int y) {
        return log(y, 0);
    }

    /**
     * <p>return log_x y</p>
     * <p>O(p^0.5)</p>
     */
    public int log(int y, int atleast) {
        y = DigitUtils.mod(y, mod);
        long start = y;
        for (int i = 0; i * m < mod; start = start * invM % mod, i++) {
            int val = map.getOrDefault((int) start, -1);
            if (val >= 0) {
                int ans = (int) (((long) val + i * m) % phi);
                if(ans >= atleast){
                    return ans;
                }
            }
        }
        return -1;
    }
}