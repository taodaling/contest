package on2021_05.on2021_05_23_Library_Checker.Inv_of_Formal_Power_Series;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;

public class InvOfFormalPowerSeries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
        }
        IntPoly poly = new IntPolyNTT(998244353);
        int[] b = poly.inverse(a, n);
        for (int i = 0; i < n; i++) {
            out.append(b.length <= i ? 0 : b[i]).append(' ');
        }
    }
}
