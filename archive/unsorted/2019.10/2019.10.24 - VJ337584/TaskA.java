package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

public class TaskA {

    int[][] dp;
    int a;
    int b;
    int c;
    int d;
    int n;

    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    Factorial fact = new Factorial(2000, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();

        dp = new int[n + 1][n + 1];
        ArrayUtils.deepFill(dp, -1);

        int ans = dp(a, n);
        out.println(ans);
    }

    public int dp(int i, int j) {
        if (i > b) {
            return j == 0 ? 1 : 0;
        }
        if (dp[i][j] == -1) {
            dp[i][j] = dp(i + 1, j);
            for(int k = c; k <= d; k++){
                if(k * i > j){
                    break;
                }
                int contrib = mod.mul(fact.fact(j),
                        fact.invFact(j - k * i));
                contrib = mod.mul(contrib, pow.pow(fact.invFact(i), k));
                contrib = mod.mul(contrib, fact.invFact(k));
                contrib = mod.mul(contrib, dp(i + 1, j - k * i));
                dp[i][j] = mod.plus(dp[i][j], contrib);
            }
        }
        return dp[i][j];
    }
}
