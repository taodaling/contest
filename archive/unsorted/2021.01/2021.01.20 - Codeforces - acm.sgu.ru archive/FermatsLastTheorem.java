package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.KthRootModPrime;
import template.math.Power;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class FermatsLastTheorem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        n %= p - 1;
        Power pow = new Power(p);
        for (int i = 0; i < 10; i++) {
            int x = RandomWrapper.INSTANCE.nextInt(1, p - 1);
            int y = RandomWrapper.INSTANCE.nextInt(1, p - 1);
            int zn = (pow.pow(x, n) + pow.pow(y, n)) % p;
            if (zn == 0) {
                continue;
            }
            long z = KthRootModPrime.kth_root(zn, n, p);
            if (z == -1) {
                continue;
            }
            out.append(x).append(' ').append(y).append(' ').println(z);
            return;
        }
        out.println(-1);
    }
}
