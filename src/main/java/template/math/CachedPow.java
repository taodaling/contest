package template.math;

public class CachedPow {
    private int[] first;
    private int[] second;
    private Modular mod;
    private Modular powMod;

    public CachedPow(int x, Modular mod) {
        this(x, mod.getMod(), mod);
    }

    public CachedPow(int x, int maxExp, Modular mod) {
        this.mod = mod;
        this.powMod = mod.getModularForPowerComputation();
        int k = Math.max(1, (int) DigitUtils.round(Math.sqrt(maxExp)));
        first = new int[k];
        second = new int[maxExp / k + 1];
        first[0] = 1;
        for (int i = 1; i < k; i++) {
            first[i] = mod.mul(x, first[i - 1]);
        }
        second[0] = 1;
        int step = mod.mul(x, first[k - 1]);
        for (int i = 1; i < second.length; i++) {
            second[i] = mod.mul(second[i - 1], step);
        }
    }

    public int pow(int exp) {
        return mod.mul(first[exp % first.length], second[exp / first.length]);
    }

    /**
     * return x^{-exp}
     */
    public int inverse(int exp) {
        return pow(powMod.valueOf(-exp));
    }
}
