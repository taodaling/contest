package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ILongModular;
import template.math.LongPower;
import template.polynomial.FastWalshHadamardTransform;

public class DLittlePonyAndElementsOfHarmony {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        long t = in.rl();
        int p = in.ri();
        int n = 1 << m;
        ILongModular mod = ILongModular.getInstance((long) n * p);
        long[] e = new long[n];
        for (int i = 0; i < n; i++) {
            e[i] = in.ri();
        }
        long[] b = new long[m + 1];
        for (int i = 0; i < m + 1; i++) {
            b[i] = in.ri();
        }

        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = b[Integer.bitCount(i)];
        }
        LongPower pow = new LongPower(mod);
        FastWalshHadamardTransform.xorFWT(e, n, mod.getMod(), false);
        FastWalshHadamardTransform.xorFWT(a, n, mod.getMod(), false);
        for (int i = 0; i < n; i++) {
            a[i] = pow.pow(a[i], t);
        }
        FastWalshHadamardTransform.dotMul(e, a, e, 0, n - 1, mod);
        FastWalshHadamardTransform.xorFWT(e, n, mod.getMod(), true);
        for (int i = 0; i < n; i++) {
            out.append(e[i] % p).println();
        }
    }

}
