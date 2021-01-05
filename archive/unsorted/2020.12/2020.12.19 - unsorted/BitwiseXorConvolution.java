package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class BitwiseXorConvolution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = 998244353;
        int[] a = new int[1 << n];
        int[] b = new int[1 << n];
        in.populate(a);
        in.populate(b);
        FastWalshHadamardTransform.xorFWT(a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.xorFWT(b, 0, a.length - 1, mod);
        FastWalshHadamardTransform.dotMul(a, b, a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.xorIFWT(a, 0, a.length - 1, mod);
        for (int x : a) {
            out.append(x).append(' ');
        }
    }
}
