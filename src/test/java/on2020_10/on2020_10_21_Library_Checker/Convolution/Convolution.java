package on2020_10.on2020_10_21_Library_Checker.Convolution;



import template.io.FastInput;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class Convolution {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];
        in.populate(a);
        in.populate(b);
        int[] c = new IntPolyNTT(mod).convolution(a, b);
        for(int i = 0; i < n + m - 1; i ++){
            out.print(i >= c.length ? 0 : c[i]);
            out.append(' ');
        }
    }
}
