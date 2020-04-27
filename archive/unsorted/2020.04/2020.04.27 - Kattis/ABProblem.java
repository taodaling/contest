package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.FastFourierTransform;

public class ABProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int zero = 50000;
        int limit = zero * 2;
        long[] cnts = new long[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt() + zero]++;
        }

        //inclusion-exclusion
        //ij ik jk
        //000
        long ans000 = 0;
        int proper = Log2.ceilLog(limit + limit + 1);
        double[][] p = new double[2][1 << proper];
        for (int i = 0; i <= limit; i++) {
            p[0][i] = cnts[i];
        }
        FastFourierTransform.dft(p, proper);
        FastFourierTransform.dotMul(p, p, p, proper);
        FastFourierTransform.idft(p, proper);
        for (int i = 0; i <= limit; i++) {
            long cnt = DigitUtils.round(p[0][i + zero]);
            ans000 += cnt * cnts[i];
        }

        //
        long ans100 = 0;
        for (int i = 0; i <= limit; i++) {
            int k = i + i - zero;
            if (k >= 0 && k <= limit) {
                ans100 += cnts[i] * cnts[k];
            }
        }

        //
        long ans010 = 0;
        for (int i = 0; i <= limit; i++) {
            ans010 += cnts[i] * cnts[zero];
        }

        //
        long ans001 = ans010;

        long ans110 = cnts[zero];
        long ans011 = ans110;
        long ans101 = ans110;
        long ans111 = ans110;


        long ans = ans000 - ans100 - ans010 - ans001 + ans110 + ans011 + ans101 - ans111;
        out.println(ans);
    }
}
