package on2021_03.on2021_03_17_Library_Checker.Subset_Convolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastSubsetTransform;

public class SubsetConvolution {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(1 << n);
        int[] b = in.ri(1 << n);
        int[] c = FastSubsetTransform.mul(a, b, mod);
        for(int x : c){
            out.append(x).append(' ');
        }
    }
}
