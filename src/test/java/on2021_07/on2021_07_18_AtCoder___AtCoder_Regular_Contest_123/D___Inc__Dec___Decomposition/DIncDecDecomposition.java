package on2021_07.on2021_07_18_AtCoder___AtCoder_Regular_Contest_123.D___Inc__Dec___Decomposition;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongLinearFunction;
import template.rand.Randomized;

import java.util.Arrays;

public class DIncDecDecomposition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] A = in.rl(n);
        LongLinearFunction[] B = new LongLinearFunction[n];
        LongLinearFunction[] C = new LongLinearFunction[n];
        B[0] = new LongLinearFunction(1, 0);
        C[0] = new LongLinearFunction(-1, A[0]);
        for (int i = 1; i < n; i++) {
            B[i] = B[i - 1].clone();
            C[i] = C[i - 1].clone();
            if (A[i] > A[i - 1]) {
                B[i].b += A[i] - A[i - 1];
            } else {
                C[i].b += A[i] - A[i - 1];
            }
        }
        long[] x = new long[2 * n];
        for (int i = 0; i < n; i++) {
            x[i] = B[i].b * B[i].a;
            x[i + n] = C[i].b * C[i].a;
        }
        Randomized.shuffle(x);
        Arrays.sort(x);
        long choice = x[n];
        long ans = 0;
        for(long z : x){
            ans += Math.abs(choice - z);
        }
        out.println(ans);
    }
}
