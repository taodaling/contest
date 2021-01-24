package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;

public class SignalProcessing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        double[] a = new double[n + m];
        double[] b = new double[m];
        for (int i = 0; i < n; i++) {
            a[i + m] = in.ri();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.ri();
        }
        double[] c = FastFourierTransform.deltaConvolution(a, b);
        for (int i = 1; i < n + m; i++) {
            double v = c.length > i ? c[i] : 0;
            out.append(Math.round(v)).append(' ');
        }
    }
}
