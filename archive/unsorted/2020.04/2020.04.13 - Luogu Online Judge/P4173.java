package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class P4173 {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[] A = new char[n];
        char[] B = new char[m];
        in.readString(A, 0);
        in.readString(B, 0);

        if (n > m) {
            out.println(0);
            return;
        }

        for (int i = 0; i < n; i++) {
            if (A[i] == '*') {
                A[i] = 0;
            } else {
                A[i] = (char) (A[i] - 'a' + 1);
            }
        }
        for (int i = 0; i < m; i++) {
            if (B[i] == '*') {
                B[i] = 0;
            } else {
                B[i] = (char) (B[i] - 'a' + 1);
            }
        }
        int proper = Log2.ceilLog((n - 1 + m - 1) + 1);
        SequenceUtils.reverse(B, 0, m - 1);
        double[][][] s = new double[3][2][1 << proper];
        double[][][] p = new double[3][2][1 << proper];
        for (int i = 0; i < m; i++) {
            s[0][0][i] = B[i];
            for (int j = 1; j < 3; j++) {
                s[j][0][i] = s[j - 1][0][i] * B[i];
            }
        }
        for (int i = 0; i < n; i++) {
            p[0][0][i] = A[i];
            for (int j = 1; j < 3; j++) {
                p[j][0][i] = p[j - 1][0][i] * A[i];
            }
        }

        for (int i = 0; i < 3; i++) {
            FastFourierTransform.dft(s[i], proper);
            FastFourierTransform.dft(p[i], proper);
        }

        double[][] ans = new double[2][1 << proper];
       // debug.debug("s", s);
       // debug.debug("p", p);
        FastFourierTransform.dotMul(s[0], p[2], s[0], proper);
        FastFourierTransform.dotMul(s[1], p[1], s[1], proper);
        FastFourierTransform.dotMul(s[2], p[0], s[2], proper);
        for (int i = 0; i < (1 << proper); i++) {
            for (int j = 0; j < 2; j++) {
                ans[j][i] = s[0][j][i] - 2 * s[1][j][i] + s[2][j][i];
            }
        }

        debug.debug("ans", ans);
        FastFourierTransform.idft(ans, proper);
        SequenceUtils.reverse(ans[0], 0, m - 1);
        SequenceUtils.reverse(ans[1], 0, m - 1);
        debug.debug("ans", ans);
        boolean[] ok = new boolean[m];
        int sum = 0;
        for (int i = 0; i + n - 1 < m; i++) {
            ok[i] = ans[0][i] < 1e-3;
            if (ok[i]) {
                sum++;
            }
        }
        out.println(sum);
        for (int i = 0; i < m; i++) {
            if (ok[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
