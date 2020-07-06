package template.rand;

import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;

public class HashData {
    public Modular mod;
    public int[] inv;
    public int[] pow;


    public HashData(int n, int p, int x) {
        this.mod = new Modular(p);
        n = Math.max(n, 1);
        inv = new int[n + 1];
        pow = new int[n + 1];
        inv[0] = 1;
        pow[0] = 1;
        int invX = new Power(mod).inverse(x);
        for (int i = 1; i <= n; i++) {
            inv[i] = mod.mul(inv[i - 1], invX);
            pow[i] = mod.mul(pow[i - 1], x);
        }
    }

    public HashData(int n) {
        this(n, (int) 1e9 + 7, RandomWrapper.INSTANCE.nextInt(1, (int) 1e9 + 6));
    }

    public int hash(int x) {
        return mod.valueOf(x);
    }

    public int hash(long x) {
        long high = x >>> 32;
        long low = x & ((1L << 32) - 1);
        return mod.valueOf(high * 31L + low);
    }

    public int hash(double x) {
        return hash(Double.doubleToRawLongBits(x));
    }
}
