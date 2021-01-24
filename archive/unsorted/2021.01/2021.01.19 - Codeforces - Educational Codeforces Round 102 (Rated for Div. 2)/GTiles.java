package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;

public class GTiles {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] last = new int[]{1};
        IntPoly poly = new IntPolyNTT(mod);
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            int b = in.ri();
            int nextLevel = a - b + last.length;
            int height = a + b;
            int[] next = new int[nextLevel];
            int low = a - (nextLevel - 1);
            int high = last.length - 1 + a;
            int[] p = PrimitiveBuffers.allocIntPow2(high - low + 1);
            for (int j = low; j <= high; j++) {
                p[j - low] = comb.combination(height, j);
            }
            int[] conv = poly.deltaConvolution(p, last);
            for (int j = 0; j < nextLevel; j++) {
                int id = a - j - low;
                next[j] = conv.length <= id ? 0 : conv[id];
            }
            debug.debugArray("next", next);
            PrimitiveBuffers.release(p, conv);
            last = next;
        }

        long sum = 0;
        for (long x : last) {
            sum += x;
        }
        sum %= mod;
        out.println(sum);
    }
}
