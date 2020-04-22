package template.math;

public class CachedPow {
    private int[] first;
    private int[] second;
    private Modular mod;
    private Modular powMod;
    private static int step = 16;
    private static int limit = 1 << step;
    private static int mask = limit - 1;

    public CachedPow(int x, Modular mod) {
        this.mod = mod;
        this.powMod = mod.getModularForPowerComputation();
        first = new int[limit];
        second = new int[Integer.MAX_VALUE / limit + 1];
        first[0] = 1;
        for (int i = 1; i < first.length; i++) {
            first[i] = mod.mul(x, first[i - 1]);
        }
        second[0] = 1;
        int step = mod.mul(x, first[first.length - 1]);
        for (int i = 1; i < second.length; i++) {
            second[i] = mod.mul(second[i - 1], step);
        }
    }

    public int pow(int exp) {
        return mod.mul(first[exp & mask], second[exp >> step]);
    }

    public int pow(long exp) {
        return pow(powMod.valueOf(exp));
    }

    /**
     * return x^{-exp}
     */
    public int inverse(int exp) {
        return pow(powMod.valueOf(-exp));
    }

    public int inverse(long exp) {
        return pow(powMod.valueOf(-exp));
    }
}
