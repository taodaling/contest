package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;

import java.util.Arrays;

public class BitSubstrings {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.rs(s);
        double[] p = new double[n + 1];
        int ps = 0;
        p[ps]++;
        for (int i = 0; i < n; i++) {
            ps += s[i] - '0';
            p[ps]++;
        }
        double[] delta = FastFourierTransform.deltaConvolution(p, p);
        delta = Arrays.copyOf(delta, n + 1);
        long zero = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == '1') {
                continue;
            }
            int to = i;
            while (to + 1 < n && s[to + 1] == '0') {
                to++;
            }
            int m = to - i + 1;
            zero += choose2(m + 1);
            i = to;
        }
        out.println(zero);
        for (int i = 1; i <= n; i++) {
            out.println(Math.round(delta[i]));
        }
    }
}
