package template.math;

public class LongCachedPow {
    private long[] first;
    private long[] second;
    private ILongModular mod;
    private ILongModular powMod;

    public LongCachedPow(long x, ILongModular mod) {
        this(x, mod.getMod(), mod);
    }

    public LongCachedPow(long x, long maxExp, ILongModular mod) {
        this.mod = mod;
        this.powMod = mod.getModularForPowerComputation();
        int k = Math.max(1, (int) DigitUtils.round(Math.sqrt(maxExp)));
        first = new long[k];
        second = new long[(int) (maxExp / k + 1)];
        first[0] = 1;
        for (int i = 1; i < k; i++) {
            first[i] = mod.mul(x, first[i - 1]);
        }
        second[0] = 1;
        long step = mod.mul(x, first[k - 1]);
        for (int i = 1; i < second.length; i++) {
            second[i] = mod.mul(second[i - 1], step);
        }
    }

    public long pow(long exp) {
        return mod.mul(first[(int) (exp % first.length)], second[(int) (exp / first.length)]);
    }

    /**
     * return x^{-exp}
     */
    public long inverse(long exp) {
        return pow(powMod.valueOf(-exp));
    }
}
