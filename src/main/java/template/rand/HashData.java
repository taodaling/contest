package template.rand;

import template.math.Modular;
import template.math.Power;

public class HashData {
    public Modular mod;
    public int[] inv;
    public int[] pow;

    public HashData(int n, int p) {
        this(n, p, RandomWrapper.INSTANCE.nextInt(3, p - 1));
    }

    public HashData(int n, int p, int x) {
        this.mod = new Modular(p);
        n = Math.max(n, 1);
        inv = new int[n + 1];
        pow = new int[n + 1];
        inv[0] = 1;
        pow[0] = 1;
        int invX = new Power(mod).inverseByFermat(x);
        for (int i = 1; i <= n; i++) {
            inv[i] = mod.mul(inv[i - 1], invX);
            pow[i] = mod.mul(pow[i - 1], x);
        }
    }
}
