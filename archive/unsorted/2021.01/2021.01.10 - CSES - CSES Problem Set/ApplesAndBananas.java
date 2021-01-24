package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;

public class ApplesAndBananas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int n = in.ri();
        int m = in.ri();
        double[] a = new double[k + 1];
        for (int i = 0; i < n; i++) {
            a[in.ri()]++;
        }
        double[] b = new double[k + 1];
        for (int i = 0; i < m; i++) {
            b[in.ri()]++;
        }
        double[] c = FastFourierTransform.convolution(a, b);
        for (int i = 2; i <= 2 * k; i++) {
            double v = c.length > i ? c[i] : 0;
            out.append(Math.round(v)).append(' ');
        }
    }
}
