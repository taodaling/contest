package on2021_08.on2021_08_01_DMOJ___An_Animal_Contest_3.P7___Monkey_Lasers;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class P7MonkeyLasers {
    public char apply(char c, int x){
        if(x == 1){
            return c == 'L' ? 'R' : 'L';
        }
        return c;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] c = in.ri(n);
        for(int i = 0; i < n; i++){
            c[i]--;
        }
        char[] d = in.rs().toCharArray();
        int[] k = in.ri(n + 1);
        long[][][] dp = new long[2][n + 1][m];
        SequenceUtils.deepFill(dp, (long) 1e18);
        long inf = (long) 1e18;
        long minFee = inf;
        Arrays.fill(dp[0][0], 0L);
        for (int i = 0; i < n; i++) {
            minFee = Math.min(minFee, k[i] - i * 2);
            long fee = minFee + i * 2;
            for(int j = 0; j < m; j++){
                for(int t = 0; t < 2; t++){
                    dp[t ^ 1][i][j] = Math.min(dp[t ^ 1][i][j], dp[t][i][j] + fee);
                }
            }
            for (int r = 0; r < 2; r++) {
                long minusJ = inf;
                for (int j = 0; j < m; j++) {
                    minusJ = Math.min(minusJ, dp[r][i][j] - j);
                    dp[r][i][j] = j + minusJ;
                }
                long plusJ = inf;
                for (int j = m - 1; j >= 0; j--) {
                    plusJ = Math.min(plusJ, dp[r][i][j] + j);
                    dp[r][i][j] = plusJ - j;
                }
                for(int j = 0; j < m; j++){
                    if(j == c[i]){
                        continue;
                    }
                    if((apply(d[i], r) == 'L') == (j < c[i])){
                        continue;
                    }
                    dp[r][i + 1][j] = Math.min(dp[r][i + 1][j], dp[r][i][j] + 1);
                }
            }
        }
        debug.debug("dp", dp);
        long ans = inf;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < m; j++){
                ans = Math.min(ans, dp[i][n][j]);
            }
        }
        out.println(ans + 1);
    }
}
