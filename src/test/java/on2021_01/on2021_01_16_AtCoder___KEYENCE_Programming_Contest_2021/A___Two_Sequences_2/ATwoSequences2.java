package on2021_01.on2021_01_16_AtCoder___KEYENCE_Programming_Contest_2021.A___Two_Sequences_2;



import template.io.FastInput;
import template.io.FastOutput;

public class ATwoSequences2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        long[] b = in.rl(n);
        long maxA = 0;
        long maxProd = 0;
        for (int i = 0; i < n; i++) {
            maxA = Math.max(a[i], maxA);
            maxProd = Math.max(maxProd, maxA * b[i]);
            out.println(maxProd);
        }
    }
}
