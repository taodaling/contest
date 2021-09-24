package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Random_Nim_Generator;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.polynomial.FastWalshHadamardTransform;

public class RandomNimGenerator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int n = in.ri();
        int mod = 30011;
        int log = Log2.ceilLog(n + 1);
        int[] a = new int[1 << log];
        for (int i = 0; i <= n; i++) {
            a[i] = 1;
        }
        FastWalshHadamardTransform.xorFWT(a, 0, a.length - 1, mod);
        Power pow = new Power(mod);
        for(int i = 0; i < a.length; i++){
            a[i] = pow.pow(a[i], k);
        }
        FastWalshHadamardTransform.xorIFWT(a, 0, a.length - 1, mod);
        long sum = 0;
        for(int i = 1; i < a.length; i++){
            sum += a[i];
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
