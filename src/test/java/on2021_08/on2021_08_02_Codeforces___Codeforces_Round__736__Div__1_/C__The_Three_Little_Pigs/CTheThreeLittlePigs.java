package on2021_08.on2021_08_02_Codeforces___Codeforces_Round__736__Div__1_.C__The_Three_Little_Pigs;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Power;

public class CTheThreeLittlePigs {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    int inv3 = pow.inverse(3);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        Factorial fact = new Factorial(3 * n + 10, mod);
        Combination comb = new Combination(fact);
        long[][] dp = new long[3][3 * n + 1];
        dp[0][0] = dp[1][0] = dp[2][0] = n;
        long[] b = new long[3];
        for (int i = 1; i <= 3 * n; i++) {
            long sumB = 0;
            for (int j = 1; j < 3; j++) {
                b[j] = b[j - 1] + dp[j - 1][i - 1];
                sumB += b[j];
            }
            long sum = comb.combination(3 * n, i + 1);
            long x = (sum - sumB) % mod * inv3 % mod;
            for (int j = 0; j < 3; j++) {
                dp[j][i] = x + b[j];
                dp[j][i] = DigitUtils.mod(dp[j][i], mod);
            }
        }
        for(int i = 0; i < q; i++){
            int x = in.ri();
            long ans = dp[0][x] + comb.combination(3 * n, x);
            if(ans >= mod){
                ans -= mod;
            }
            out.println(ans);
        }
    }
}
