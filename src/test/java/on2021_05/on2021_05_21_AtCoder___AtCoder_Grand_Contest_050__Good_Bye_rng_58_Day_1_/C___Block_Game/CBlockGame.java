package on2021_05.on2021_05_21_AtCoder___AtCoder_Grand_Contest_050__Good_Bye_rng_58_Day_1_.C___Block_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.Debug;

public class CBlockGame {
    int mod = 998244353;

    public void update(long[] data, int l, int r, long x) {
        if (l > r || r < 0) {
            return;
        }
        data[r] += x;
        if (l > 0) {
            data[l - 1] -= x;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int L = (int) 1e6;
        int R = 25;
        char[] s = new char[L];
        int n = in.rs(s);
        int unknown = 0;
        for(int i = 0; i < n; i++){
            if(s[i] == '?'){
                unknown++;
            }
        }
        long[][] dp = new long[R][n];
        long[][] ps = new long[R][n + 1];
        int[] lastB = new int[n + 1];
        for (int i = n; i >= 0; i--) {
            lastB[i] = i - 1;
            if (i + 1 <= n) {
                lastB[i] = Math.min(lastB[i], lastB[i + 1]);
            }
            while (lastB[i] >= 0 && s[lastB[i]] != 'B') {
                lastB[i]--;
            }
        }
        update(ps[0], lastB[n], n, 1);


        Modular barrett = new Modular(mod);
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < R; j++) {
                ps[j][i] += ps[j][i + 1];
                ps[j][i] = barrett.valueOf(ps[j][i]);
                dp[j][i] += ps[j][i];
                dp[j][i] = barrett.valueOf(dp[j][i]);

                if (s[i] == 'S' || dp[j][i] == 0) {
                    continue;
                }

                //ceil((x + 1) / 2^j) > 1)
                //x + 1 > 2^j
                //x >= 2^j
                if (j + 1 < R) {
                    update(ps[j + 1], Math.max(0, lastB[i]), i - 1 - (1 << j), dp[j][i]);
                }
            }
        }

        long ans = 0;
        if(lastB[n] < 0){
            ans++;
        }
        for(int i = 0; i < R; i++){
            for(int j = 0; j < n; j++){
                if(s[j] == 'S' || lastB[j] >= 0 || dp[i][j] == 0){
                    continue;
                }
                ans += dp[i][j];
            }
        }

        ans = power.pow(2, unknown) - ans;
        debug.debug("lastB", lastB);
        debug.debug("dp", dp);
        debug.debug("ps", ps);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Power power = new Power(mod);

    Debug debug = new Debug(false);
}
