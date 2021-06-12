package template.math;

import template.binary.Log2;

public class CachedPow2 {
    private int[] first;
    private int[] second;
    private int mod;
    private int low;
    private int mask;
    private int phi;
    private int xphi;
    private int x;
    private Modular barrett;

    public CachedPow2(int x, int mod) {
        this(x, mod, CachedEulerFunction.get(mod));
    }

    public CachedPow2(int x, int mod, int phi) {
        this(x, mod, mod, phi);
    }

    public CachedPow2(int x, int mod, int limit, int phi) {
        barrett = new Modular(mod);
        this.x = x;
        this.phi = phi;
        limit = Math.min(limit, mod);
        this.mod = mod;
        int log = Log2.ceilLog(limit + 1);
        low = (log + 1) / 2;
        mask = (1 << low) - 1;
        first = new int[1 << low];
        second = new int[1 << log - low];
        first[0] = 1;
        for (int i = 1; i < first.length; i++) {
            first[i] = barrett.mul(x, first[i - 1]);
        }
        second[0] = 1;
        long step = (long) x * first[first.length - 1] % mod;
        for (int i = 1; i < second.length; i++) {
            second[i] = barrett.mul(second[i - 1], step);
        }

        xphi = DigitUtils.modPow(x, phi, mod);
    }

    private int pow0(int exp) {
        return barrett.mul(first[exp & mask], second[exp >> low]);
    }

    public int pow(long exp) {
        long fix = DigitUtils.mod(exp, phi);
        long ans;
        if (xphi == 1 || fix == exp) {
            ans = pow0((int) fix);
        } else {
            ans = barrett.mul(pow0((int) fix), xphi);
        }
        return (int) ans;
    }

    /**
     * return x^{-exp}
     */
    public int inverse(int exp) {
        return pow(DigitUtils.negate(exp, phi));
    }

    public int inverse(long exp) {
        return pow(DigitUtils.mod(-exp, phi));
    }
}
