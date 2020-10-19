package template.math;

public class CachedPow {
    private int[] first;
    private int[] second;
    private int mod;
    private int powMod;
    private static int step = 16;
    private static int limit = 1 << step;
    private static int mask = limit - 1;

    public CachedPow(int x, int mod) {
        this.mod = mod;
        this.powMod = mod - 1;
        first = new int[limit];
        second = new int[Integer.MAX_VALUE / limit + 1];
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
        return (int) ((long) first[exp & mask] * second[exp >> step] % mod);
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
