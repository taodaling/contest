package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class CountingTowers {
    int mod = (int) 1e9 + 7;
    long[] A = new long[(int) 1e6 + 1];
    long[] B = new long[(int) 1e6 + 1];

    {
        A[1] = 1;
        B[1] = 1;
        for (int i = 2; i <= (int) 1e6; i++) {
            A[i] = A[i - 1] * 2 + B[i - 1];
            B[i] = A[i - 1] + B[i - 1] * 4;
            A[i] %= mod;
            B[i] %= mod;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        out.println((A[n] + B[n]) % mod);
    }
}
