package on2020_04.on2020_04_03_Codeforces_Round__631__Div__1____Thanks__Denis_aramis_Shitov_.B__Dreamoon_Likes_Sequences;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

public class BDreamoonLikesSequences {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int d = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(m);
        Power power = new Power(mod);
        int log = Log2.floorLog(d);
        int[][] dp = new int[log + 1][log + 1];
        int[] possible = new int[log + 1];
        possible[0] = 1;
        for (int i = 1; i < log; i++) {
            possible[i] = power.pow(2, i);
        }
        possible[log] = d - (1 << log) + 1;


        for (int i = 0; i <= log; i++) {
            for (int j = 0; j <= log; j++) {
                int pre = i == 0 ? 1 : j == 0 ? 0 : dp[i - 1][j - 1];
                dp[i][j] = mod.plus(dp[i][j], mod.mul(pre, possible[j]));
            }
            for (int j = 1; j <= log; j++) {
                dp[i][j] = mod.plus(dp[i][j - 1], dp[i][j]);
            }
        }
        debug.debug("possible", possible);
        debug.debug("dp", dp);
        int ans = 0;
        for (int i = 0; i <= log; i++) {
            ans = mod.plus(ans, dp[i][log]);
        }
        out.println(ans);
    }
}
