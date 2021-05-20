package on2021_05.on2021_05_20_Library_Checker.Convolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Barrett;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;

public class Convolution {
    int mod = 998244353;
    Barrett modular = new Barrett(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(m);

        IntPoly poly = new IntPolyNTT(mod);
        int[] c = poly.convolution(a, b);
        for (int i = 0; i <= n - 1 + m - 1; i++) {
            int v;
            if (i >= c.length) {
                v = 0;
            } else {
                v = c[i];
            }
            out.append(v).append(' ');
        }
    }
}
