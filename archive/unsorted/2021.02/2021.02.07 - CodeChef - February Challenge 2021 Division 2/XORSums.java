package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;

public class XORSums {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int q = in.ri();
        IntPoly poly = new IntPolyFFT(mod);
        long[] sum = new long[n + 1];
        for (int i = 0; i < 30; i++) {
            int[] cnts = new int[2];
            for (int x : a) {
                cnts[Bits.get(x, i)]++;
            }
            int[] f = PrimitiveBuffers.allocIntPow2(cnts[1] + 1);
            int[] g = PrimitiveBuffers.allocIntPow2(cnts[0] + 1);
            for (int j = 1; j <= cnts[1]; j += 2) {
                f[j] = comb.combination(cnts[1], j);
            }
            for(int j = 0; j <= cnts[0]; j++){
                g[j] = comb.combination(cnts[0], j);
            }
            int[] conv = poly.convolution(f, g);
           // debug.debug("conv", conv);
            long ps = 0;
            for(int j = 0; j <= n; j++){
                int v = j >= conv.length ? 0 : conv[j];
                ps += v;
                if(ps >= mod){
                    ps -= mod;
                }
                sum[j] += (ps << i) % mod;
            }
            PrimitiveBuffers.release(f, g, conv);
        }
        for(int i = 0; i <= n; i++){
            sum[i] %= mod;
        }
        for(int i = 0; i < q; i++){
            int m = in.ri();
            out.println(sum[m]);
        }
    }
}
