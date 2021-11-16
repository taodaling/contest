package on2021_11.on2021_11_11_CS_Academy___FIICode_2021_Final_Round.Final_C;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class FinalC {
    int mod = (int)1e9 + 7;
    Combination comb = new Combination((int)33, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[][] dp = new long[33][n + 1];
        long[][] fastComb = new long[33][33];
        for(int i = 0; i <= 32; i++){
            for(int j = 0; j <= 32; j++){
                fastComb[i][j] = comb.combination(i, j);
            }
        }
        dp[0][0] = 1;
        for(int i = 0; i < n; i++){
            for(int j = 0; j <= 32; j++){
                dp[j][i] %= mod;
                if(dp[j][i] == 0){
                    continue;
                }
                for(int k = 0; k + j <= 32; k++){
                    dp[k][i + 1] += dp[j][i] * fastComb[32 - j][k] % mod;
                }
            }
        }

        long ans = 0;
        for(int i = 0; i <= n; i++){
            for(int j = 0; j <= 32; j++){
                ans += dp[j][i];
            }
        }
        ans = DigitUtils.mod(ans - 1, mod);
        out.println(ans);
    }
}
