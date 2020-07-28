package on2020_07.on2020_07_28_AtCoder___Yahoo_Programming_Contest_2019.F___Pass;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerPreSum;

public class FPass {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(998244353);
        char[] s = in.readString().toCharArray();
        int n = s.length;

        int[][] dp = new int[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                int b = j;
                int r = i - j;
                int way = dp[i][j];
                if (way == 0) {
                    continue;
                }
                if (s[i] == '0') {
                    //two blue
                    //this is blue
                    dp[i + 1][j + 1] = mod.plus(dp[i + 1][j + 1], way);
                    //this is red
                    if (r > 0) {
                        dp[i + 1][j + 2] = mod.plus(dp[i + 1][j + 2], way);
                    }
                } else if (s[i] == '1') {
                    //one blue one red
                    //this is blue
                    dp[i + 1][j] = mod.plus(dp[i + 1][j], way);
                    //this is red
                    dp[i + 1][j + 1] = mod.plus(dp[i + 1][j + 1], way);
                } else {
                    //two red
                    //this is blue
                    if (j > 0) {
                        dp[i + 1][j - 1] = mod.plus(dp[i + 1][j - 1], way);
                    }
                    //this is red
                    dp[i + 1][j] = mod.plus(dp[i + 1][j], way);
                }
            }
        }

        int ans = 0;
        Combination comb = new Combination(n, new Power(mod));
        for(int i = 0; i <= n; i++){
            int way = dp[n][i];
            way = mod.mul(way, comb.combination(n, i));
            ans = mod.plus(ans, way);
        }
        out.println(ans);
    }
}
