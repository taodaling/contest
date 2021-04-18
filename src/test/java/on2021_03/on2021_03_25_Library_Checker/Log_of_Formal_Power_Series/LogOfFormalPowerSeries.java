package on2021_03.on2021_03_25_Library_Checker.Log_of_Formal_Power_Series;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;

public class LogOfFormalPowerSeries {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
        }
        IntPoly poly = new IntPolyFFT(mod);
        int[] b = poly.ln(a, n);
        for (int i = 0; i < n; i++) {
            out.append(b.length <= i ? 0 : b[i]).append(' ');
        }
    }
}
