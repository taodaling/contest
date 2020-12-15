package on2020_12.on2020_12_14_Library_Checker.Bitwise_And_Convolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class BitwiseAndConvolution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = 998244353;
        int[] a = new int[1 << n];
        int[] b = new int[1 << n];
        in.populate(a);
        in.populate(b);
        FastWalshHadamardTransform.andFWT(a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.andFWT(b, 0, a.length - 1, mod);
        FastWalshHadamardTransform.dotMul(a, b, a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.andIFWT(a, 0, a.length - 1, mod);
        for(int x : a){
            out.append(x).append(' ');
        }
    }
}
