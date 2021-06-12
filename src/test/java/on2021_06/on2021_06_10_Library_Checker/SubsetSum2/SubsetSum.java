package on2021_06.on2021_06_10_Library_Checker.SubsetSum2;





import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.problem.CountSubsetSum;
import template.utils.PrimitiveBuffers;

public class SubsetSum {
    int mod = 998244353;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int t = in.ri();
        int[] s = in.ri(n);
        CountSubsetSum sum = new CountSubsetSum(s, t, mod, new IntPolyFFT(mod));
        for(int i = 1; i <= t; i++){
            out.println(sum.query(i));
        }
    }
}
