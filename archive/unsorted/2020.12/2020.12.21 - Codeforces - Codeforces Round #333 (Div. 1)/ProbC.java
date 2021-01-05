package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class ProbC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] x = new int[n];
        in.populate(x);
        if (m == 1) {
            out.println(1);
            return;
        }
        for (int i = 0; i < n; i++) {
            x[i]--;
        }
        int sum = Arrays.stream(x).sum();
        double[][] polys = new double[n][];
        for (int i = 0; i < n; i++) {
            polys[i] = PrimitiveBuffers.allocDoublePow2(m);
            for (int j = 0; j < m; j++) {
                if (j == x[i]) {
                    continue;
                }
                polys[i][j] = 1.0 / (m - 1);
            }
        }
        double[] merged = mul(polys, 0, n - 1);
        double prob = 0;
        for (int i = 0; i < sum; i++) {
            prob += merged.length <= i ? 0 : merged[i];
        }
        double ans = prob * (m - 1) + 1;
        out.println(ans);
    }

    public double[] mul(double[][] polys, int l, int r) {
        if (l == r) {
            return polys[l];
        }
        int m = (l + r) / 2;
        double[] a = mul(polys, l, m);
        double[] b = mul(polys, m + 1, r);
        double[] ans = FastFourierTransform.mul(a, b);
        PrimitiveBuffers.release(a, b);
        return ans;
    }
}
