package template.math;

import template.binary.Log2;

public class CachedPow2 {
    private int[] first;
    private int[] second;
    private int mod;
    private int powMod;
    private int low;
    private int mask;

    public CachedPow2(int x, int mod) {
        this(x, mod, mod);
    }

    public CachedPow2(int x, int mod, int limit) {
        this.mod = mod;
        this.powMod = mod - 1;
        int log = Log2.ceilLog(limit + 1);
        low = (log + 1) / 2;
        mask = (1 << low) - 1;
        first = new int[1 << low];
        second = new int[1 << log - low];
        first[0] = 1;
        for (int i = 1; i < first.length; i++) {
            first[i] = (int) ((long) x * first[i - 1] % mod);
        }
        second[0] = 1;
        long step = (long) x * first[first.length - 1] % mod;
        for (int i = 1; i < second.length; i++) {
            second[i] = (int) (second[i - 1] * step % mod);
        }
    }

    public int pow(int exp) {
        return (int) ((long) first[exp & mask] * second[exp >> low] % mod);
    }

    public int pow(long exp) {
        return pow(DigitUtils.mod(exp, powMod));
    }

    /**
     * return x^{-exp}
     */
    public int inverse(int exp) {
        return pow(DigitUtils.negate(exp, powMod));
    }

    public int inverse(long exp) {
        return pow(DigitUtils.mod(-exp, powMod));
    }
}
