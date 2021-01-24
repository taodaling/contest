package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;

public class OneBitPositions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.rs(s);
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            if (s[i] == '1') {
                a[i] = 1;
            }
        }

        double[] c = FastFourierTransform.deltaConvolution(a, a);
        for (int i = 1; i < n; i++) {
            double v = c.length > i ? c[i] : 0;
            out.append(Math.round(v)).append(' ');
        }
    }
}
