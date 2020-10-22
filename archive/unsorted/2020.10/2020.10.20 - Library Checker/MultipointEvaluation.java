package contest;

import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class MultipointEvaluation {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int mod = 998244353;
        int[] c = PrimitiveBuffers.allocIntPow2(n);
        int[] x = new int[m];
        for (int i = 0; i < n; i++) {
            c[i] = in.readInt();
        }
        in.populate(x);
        int[] y = new int[m];
        IntPoly poly = new IntPolyFFT(mod);
        poly.multiApply(c, x, y, m);
        for (int i = 0; i < m; i++) {
            out.println(y[i]);
        }
    }
}
