package on2021_06.on2021_06_09_Codeforces___XXI_Opencup_GP_of_Tokyo.B__Bit_Operation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class BBitOperation {
    int mod = 998244353;
    Combination comb = new Combination((int)1e6, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] f = new long[n];
        f[0] = 1;
        for(int i = 1; i < n; i++){
            f[i] = f[i - 1] * ((i - 1) * 2 + 1) % mod;
        }
        int[] a = in.ri(n);
        long ans = 0;
        for(int i = 0; i < n; i++){
            if(a[i] == 0){
                continue;
            }
            int L = i;
            int R = n - 1 - L;
            long contrib = f[L] * f[R] % mod * comb.combination(L + R, L) % mod;
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
