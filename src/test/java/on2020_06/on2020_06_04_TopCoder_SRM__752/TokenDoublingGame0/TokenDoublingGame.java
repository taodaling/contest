package on2020_06.on2020_06_04_TopCoder_SRM__752.TokenDoublingGame0;



import template.math.CachedPow;
import template.math.LinearFunction;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

public class TokenDoublingGame {
    //Debug debug = new Debug(true);

    public int expectation(int N) {
        LinearFunction[] dp = new LinearFunction[N * 2 + 1];
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        CachedPow cp = new CachedPow(2, mod);
        int half = cp.inverse(1);
        dp[1] = LinearFunction.IDENTITY;
        dp[0] = LinearFunction.ZERO;
        for (int i = 2; i <= 2 * N; i++) {
            dp[i] = dp[i - 1];
            int prob = half;
            for (int j = 0; ; j++) {
                prob = mod.mul(prob, half);
                int step = 2 + j;
                if (i - 1 - ((1 << (j + 1)) - 1) <= 0) {
                    step = mod.subtract(step, 1);
                    prob = mod.mul(prob, 2);
                    //dp[0]
                    dp[i] = LinearFunction.subtract(dp[i], 0, mod.mul(step, prob), mod);
                    break;
                } else {
                    int k = i - 1 - ((1 << (j + 1)) - 1) + 1;
                    dp[i] = LinearFunction.subtract(dp[i],
                            LinearFunction.mul(
                                    LinearFunction.plus(dp[k], 0, step, mod),
                                    prob, mod),
                            mod);
                }
            }
            dp[i] = LinearFunction.mul(dp[i], 2, mod);
            dp[i] = LinearFunction.subtract(dp[i], 0, 1, mod);
        }

        //dp[2n] = 0
        LinearFunction lf = LinearFunction.inverse(dp[2 * N], mod, power);
        int x = lf.apply(0, mod);
        int ans = dp[N].apply(x, mod);
       // debug.debug("dp", dp);
       // debug.debug("x", x);
//        for(int i = 0; i <= 2 * N; i++){
//         //   debug.debug("i", i);
//           // debug.debug("dp[i]", dp[i].apply(x, mod));
//        }
        return ans;
    }
}
