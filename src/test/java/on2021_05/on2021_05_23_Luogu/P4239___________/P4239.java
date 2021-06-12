package on2021_05.on2021_05_23_Luogu.P4239___________;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyMTT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

public class P4239 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = PrimitiveBuffers.allocIntPow2(n);
        for(int i = 0; i < n; i++){
            a[i] = in.ri();
        }
        IntPoly poly = new IntPolyFFT((int)1e9 + 7);
        int[] b = poly.inverse(a, n);
        for(int i = 0; i < n; i++){
            int val = b.length <= i ? 0 : b[i];
            out.append(val).append(' ');
        }
    }
}
