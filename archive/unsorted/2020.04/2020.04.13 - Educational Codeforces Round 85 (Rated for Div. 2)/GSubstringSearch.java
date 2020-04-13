package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.FastFourierTransform;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class GSubstringSearch {
    Debug debug = new Debug(true);

    public long pow(int x, int n) {
        return n == 0 ? 1 : x * pow(x, n - 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] perm = new int[26];
        for (int i = 0; i < 26; i++) {
            perm[i] = in.readInt() - 1;
        }
        int[] a = new int[(int) 2e5];
        int[] s = new int[(int) 2e5];
        int n = in.readString(a, 0);
        int m = in.readString(s, 0);

        for (int i = 0; i < n; i++) {
            a[i] -= 'a';
        }
        for (int i = 0; i < m; i++) {
            s[i] -= 'a';
        }

        SequenceUtils.reverse(s, 0, m - 1);
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[i] = perm[a[i]];
        }

        int proper = Log2.ceilLog(n + m - 1);
        double[][][] s3 = new double[3][2][1 << proper];
        double[][][] right = new double[3][2][1 << proper];
        for (int i = 0; i < n; i++) {
            right[0][0][i] = -2 * a[i] * b[i] * (a[i] + b[i]);
            right[1][0][i] = pow(a[i] + b[i], 2) + 2 * a[i] * b[i];
            right[2][0][i] = -2 * (a[i] + b[i]);
        }
        for (int i = 0; i < m; i++) {
            s3[0][0][i] = s[i];
            for (int j = 1; j < 3; j++) {
                s3[j][0][i] = s3[j - 1][0][i] * s[i];
            }
        }

        for (int i = 0; i < 3; i++) {
            FastFourierTransform.dft(s3[i], proper);
            FastFourierTransform.dft(right[i], proper);
            FastFourierTransform.dotMul(s3[i], right[i], s3[i], proper);
        }

        for (int i = 0; i < (1 << proper); i++) {
            for (int j = 0; j < 2; j++) {
                s3[0][j][i] = s3[0][j][i] + s3[1][j][i] + s3[2][j][i];
            }
        }

        FastFourierTransform.idft(s3[0], proper);
        SequenceUtils.reverse(s3[0][0], 0, m - 1);
        SequenceUtils.reverse(s3[0][1], 0, m - 1);
        SequenceUtils.reverse(s, 0, m - 1);

        long[] s4 = new long[m];
        int l = 0;
        int r = -1;
        long sum = 0;
        for (int i = 0; i + n - 1 < m; i++) {
            while (r - i + 1 < n) {
                r++;
                sum += s[r] * s[r] * s[r] * s[r];
            }
            while (l < i) {
                sum -= s[l] * s[l] * s[l] * s[l];
                l++;
            }
            s4[i] = sum;
        }

        long fix = 0;
        for (int i = 0; i < n; i++) {
            fix += a[i] * a[i] * b[i] * b[i];
        }

        boolean[] ans = new boolean[m];
        for (int i = 0; i + n - 1 < m; i++) {
            double sub = s4[i] + fix + s3[0][0][i];
            ans[i] = DigitUtils.round(sub) == 0;
        }

        for (int i = 0; i + n - 1 < m; i++) {
            if (ans[i]) {
                out.append(1);
            } else {
                out.append(0);
            }
        }
    }
}
