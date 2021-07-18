package on2021_07.on2021_07_18_Library_Checker.Convolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;

public class Convolution {
    int mod = 998244353;

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
