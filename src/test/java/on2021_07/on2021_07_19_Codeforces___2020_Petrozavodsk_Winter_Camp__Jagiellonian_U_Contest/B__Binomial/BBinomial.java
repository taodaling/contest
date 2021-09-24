package on2021_07.on2021_07_19_Codeforces___2020_Petrozavodsk_Winter_Camp__Jagiellonian_U_Contest.B__Binomial;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

import java.util.Arrays;

public class BBinomial {
    long[] c = new long[1 << 20];
    long[] subset = new long[1 << 20];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Arrays.fill(c, 0);
        Arrays.fill(subset, 0);
        for (int i = 0; i < n; i++) {
            c[in.ri()]++;
        }
        System.arraycopy(c, 0, subset, 0, c.length);
        FastWalshHadamardTransform.orFWT(subset, 0, subset.length - 1);
        long ans = 0;
        for (int i = 0; i < c.length; i++) {
            long contrib = c[i] * (subset[i] - c[i]);
            ans += contrib;
        }
        for (int i = 0; i < c.length; i++) {
            ans += c[i] * c[i];
        }
        out.println(ans);
    }
}
