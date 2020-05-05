package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.polynomial.FastWalshHadamardTransform;
import template.utils.Debug;

public class ERandomElections {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] results = new int[1 << n];
        for (int i = 0; i < (1 << n); i++) {
            results[i] = in.readChar() - '0';
        }

        long[] cnts = new long[1 << (n)];
        for (int i = 0; i < (1 << n); i++) {
            if (results[i] == 1) {
                cnts[i] = 1;
            }
        }
        debug.debug("cnts", cnts);

        FastWalshHadamardTransform.xorFWT(cnts, 0, (1 << n) - 1);
        FastWalshHadamardTransform.dotMul(cnts, cnts, cnts, 0, (1 << n) - 1);
        FastWalshHadamardTransform.xorIFWT(cnts, 0, (1 << n) - 1);

        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int div3 = power.inverseByFermat(3);
        int div6 = power.inverseByFermat(6);
        debug.debug("cnts", cnts);
        int ans = 0;
        for (int i = 0; i < (1 << n); i++) {
            int way = mod.valueOf(cnts[i]);
            int prob = 1;//mod.mul(way, divn);
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 1) {
                    prob = mod.mul(prob, div6);
                } else {
                    prob = mod.mul(prob, div3);
                }
            }
            ans = mod.plus(ans, mod.mul(prob, way));
        }

        ans = mod.mul(3, ans);
        ans = mod.mul(ans, power.pow(6, n));
        out.println(ans);
    }
}
