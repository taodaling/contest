package on2022_01.on2022_01_06_Library_Checker.Kth_term_of_Linearly_Recurrent_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KthTermOfLinearRecurrence;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;

public class KthTermOfLinearlyRecurrentSequence {
    int mod = 998244353;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int d = in.ri();
        long k = in.rl();
        int[] a = in.ri(d);
        int[] c = new int[d + 1];
        c[0] = 1;
        for(int i = 0; i < d; i++){
            c[i + 1] = DigitUtils.negate(in.ri(), mod);
        }
        KthTermOfLinearRecurrence kt = new KthTermOfLinearRecurrence(c, a, mod, new IntPolyFFT(mod));
        int ans = kt.kth(k);
        out.println(ans);
    }
}
