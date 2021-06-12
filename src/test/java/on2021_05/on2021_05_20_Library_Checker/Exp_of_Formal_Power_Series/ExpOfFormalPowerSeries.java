package on2021_05.on2021_05_20_Library_Checker.Exp_of_Formal_Power_Series;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

public class ExpOfFormalPowerSeries {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
        }
        IntPoly poly = new IntPolyNTT(mod);
        int[] b = poly.exp(a, n);
        for (int i = 0; i < n; i++) {
            out.append(b.length <= i ? 0 : b[i]).append(' ');
        }
//        PrimitiveBuffers.release(b);
//        PrimitiveBuffers.check();
    }
}