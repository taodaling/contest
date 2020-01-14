package on2020_01.on2020_01_14_Educational_Codeforces_Round_80__Rated_for_Div__2_.C__Two_Arrays;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class CTwoArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        int n = in.readInt();
        int m = in.readInt();
        int[][] dp = new int[m * 2 + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= m * 2; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 0; k <= j; k++) {
                    dp[i][j] = mod.plus(dp[i][j], dp[i - 1][k]);
                }
            }
        }

        int ans = 0;
        for(int i = 0; i <= n; i++){
            ans = mod.plus(ans, dp[m * 2][i]);
        }

        out.println(ans);
    }
}
