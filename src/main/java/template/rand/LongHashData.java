package template.rand;

import template.math.ILongModular;
import template.math.LongPower;

public class LongHashData {
    public ILongModular mod;
    public long[] inv;
    public long[] pow;


    public LongHashData(int n, long p, long x) {
        this.mod = ILongModular.getInstance(p);
        n = Math.max(n, 1);
        inv = new long[n + 1];
        pow = new long[n + 1];
        inv[0] = 1 % p;
        pow[0] = 1 % p;
        long invX = new LongPower(mod).inverse(x);
        for (int i = 1; i <= n; i++) {
            inv[i] = mod.mul(inv[i - 1], invX);
            pow[i] = mod.mul(pow[i - 1], x);
        }
    }

    public LongHashData(int n) {
        this(n, (1L << 61) - 1, RandomWrapper.INSTANCE.nextLong(3, (1L << 61) - 2));
    }
}
