package contest;

import template.math.DigitUtils;
import template.rand.Randomized;

import java.util.Arrays;

public class NRooksPosition {
    public long minSteps(int N, int[] Xprefix, int[] Yprefix, int A, int B, int C) {
        int L = Xprefix.length;
        long[] X = new long[N];
        long[] Y = new long[N];
        for (int i = 0; i <= L - 1; i++) {
            X[i] = Xprefix[i];
            Y[i] = Yprefix[i];
        }
        for (int i = L; i <= N - 1; i++) {
            X[i] = (A * X[i - 1] + B * X[i - 2]) % C;
            Y[i] = (A * Y[i - 1] + B * Y[i - 2]) % C;
        }
        Randomized.shuffle(X);
        Randomized.shuffle(Y);
        Arrays.sort(X);
        Arrays.sort(Y);
        int middle = N / 2;
        long top = X[middle] - middle;
        long left = Y[middle] - middle;
        long sum = 0;
        for (int i = 0; i < N; i++) {
            sum += Math.abs(X[i] - (top + i));
            sum += Math.abs(Y[i] - (left + i));
        }
        return sum;
    }
}
