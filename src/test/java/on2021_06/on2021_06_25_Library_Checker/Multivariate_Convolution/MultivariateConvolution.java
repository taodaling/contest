package on2021_06.on2021_06_25_Library_Checker.Multivariate_Convolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

public class MultivariateConvolution {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int[] base = in.ri(k);
        int N = 1;
        for (int x : base) {
            N *= x;
        }
        int[] f = PrimitiveBuffers.allocIntPow2(N);
        int[] g = PrimitiveBuffers.allocIntPow2(N);
        for (int i = 0; i < N; i++) {
            f[i] = in.ri();
        }
        for (int i = 0; i < N; i++) {
            g[i] = in.ri();
        }
        IntPolyNTT poly = new IntPolyNTT(mod);
        int[] fg = poly.multivariateConvolution(f, g, base);
        for(int i = 0; i < N; i++){
            out.append(i < fg.length ? fg[i] : 0).append(' ');
        }
    }
}
