package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_212.H___Nim_Counting;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.polynomial.FastWalshHadamardTransform;

public class HNimCounting {
    int mod = 998244353;
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int L = 1 << 16;
        int[] a = new int[L];
        for (int i = 0; i < k; i++) {
            a[in.ri()]++;
        }
        FastWalshHadamardTransform.xorFWT(a, 0, L - 1, mod);
        for (int i = 0; i < L; i++) {
            int x = a[i];
            int sum;
            if (x == 1) {
                sum = n;
            } else {
                sum = DigitUtils.mod((long) (power.pow(x, n + 1) - x) * power.inverse(DigitUtils.mod(x - 1, mod)), mod);
            }
            a[i] = sum;
        }
        FastWalshHadamardTransform.xorIFWT(a, 0, L - 1, mod);
        long ans = 0;
        for(int i = 1; i < L; i++){
            ans += a[i];
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
