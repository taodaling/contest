package contest;

import template.rand.Randomized;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

public class RandomSelection {
    public double expectedMaximum(int n, int T, int seed, int Amod, int[] Aprefix) {
        long[] A = new long[n];
        for (int i = 0; i <= Aprefix.length - 1; i++) {
            A[i] = Aprefix[i];
        }
        long state = seed;
        for (int i = Aprefix.length; i <= n - 1; i++) {
            state = (1103515245 * state + 12345) % (1L << 31);
            A[i] = T + (state % Amod);
        }

        Randomized.shuffle(A);
        Arrays.sort(A);
        int headZero = 0;
        while (headZero < n && A[headZero] == 0) {
            headZero++;
        }
        A = Arrays.copyOfRange(A, headZero, A.length);
        n = A.length;
        if (n == 0) {
            return 0;
        }

        double lastM = 0;
        MathContext context = new MathContext(30, RoundingMode.HALF_EVEN);
        BigDecimal prob = new BigDecimal(1);
        double fix = 1;
        for (int i = 0; i < n; i++) {
            fix *= Math.pow(A[i], 1.0 / n);
        }

        for (int i = 0; i < n; i++) {
            prob = prob.multiply(BigDecimal.valueOf(fix / A[i]), context);
        }

        double sum = 0;
        for (int i = 0; i < n; i++) {
            int r = i;
            while (r + 1 < n && A[r + 1] == A[r]) {
                r++;
            }
            int remain = n - i;
            double exp = (double) remain / (remain + 1) * prob.multiply(BigDecimal.valueOf(A[i] / fix).pow(remain + 1, context).subtract(BigDecimal.valueOf(lastM / fix).pow(remain + 1, context), context), context).doubleValue() * fix;
            sum += exp;
            prob = prob.multiply(BigDecimal.valueOf(A[i] / fix).pow(r - i + 1, context), context);
            lastM = A[r];
            i = r;
        }

        return sum;
    }
}
