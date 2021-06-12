package on2021_06.on2021_06_11_Codeforces___Codeforces_Round__534__Div__1_.E__Radix_sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.GenericFastWalshHadamardTransform;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class ERadixSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[][] cnts = new long[10][L];
        for(int i = 0; i < n; i++){
            cnts[0][in.ri()]++;
        }

        fwt.addFWT(cnts, 0, L - 1);
        long[][] res = new long[10][L];
        fwt.pow(cnts, res, 0, L - 1, n);
        fwt.addIFWT(res, 0, L - 1, false);

        long mod = 1L << 58;
        int fiveExpFive = 1;
        for(int i = 0; i < 5; i++){
            fiveExpFive *= 5;
        }
        long inv = DigitUtils.modInverse(fiveExpFive, mod);
        for(int i = 0; i < n; i++){
            long ans = ((res[0][i] * inv) >>> 5) & (mod - 1);
            out.println(ans);
        }
    }
    GenericFastWalshHadamardTransform fwt = new GenericFastWalshHadamardTransform(10);
    int L = (int)1e5;
}
