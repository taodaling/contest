package on2020_10.on2020_10_20_Library_Checker.Polynomial_Interpolation;



import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;

import java.io.PrintWriter;

public class PolynomialInterpolation {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        int[] x = new int[n];
        int[] y = new int[n];
        in.populate(x);
        in.populate(y);
        IntPoly poly = new IntPolyFFT(mod);
        int[] ans = poly.interpolate(x, y, n);
        assert poly.rankOf(ans) <= n - 1;
        for (int i = 0; i < n; i++) {
            out.print(ans[i]);
            out.append(' ');
        }
    }
}
