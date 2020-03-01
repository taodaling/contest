package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CHills {
    public int dec(int x, int t){
        return x < t ? 0 : x - t + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = (n + 1) / 2;
        int[][][] dp = new int[3][n + 1][m + 1];
        int[] h = new int[n + 1];
        for(int i = 1; i <= n; i++){
            h[i] = in.readInt();
        }
        h[0] = (int) -1e9;
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, inf);
        dp[1][0][0] = 0;
        for(int i = 1; i <= n; i++){
            for(int j = 0; j <= m; j++){
                //build a house
                if(j > 0) {
                    dp[0][i][j] = Math.min(dp[1][i - 1][j - 1] + dec(h[i - 1], h[i]), dp[0][i][j]);
                    if(i > 1)
                    {
                        dp[0][i][j] = Math.min(dp[2][i - 1][j - 1] + dec(h[i - 2] - 1, h[i]),
                                dp[0][i][j]);
                    }
                }
                //1
                dp[1][i][j] = Math.min(dp[1][i - 1][j], dp[1][i][j]);
                if(h[i - 1] > h[i]) {
                    dp[1][i][j] = Math.min(dp[0][i - 1][j], dp[1][i][j]);
                }
                dp[1][i][j] = Math.min(dp[2][i - 1][j], dp[1][i][j]);

                //2
                dp[2][i][j] = Math.min(dp[0][i - 1][j] + dec(h[i], h[i - 1]), dp[2][i][j]);
            }
        }

//        for(int i = 0; i < 3; i++){
//            for(int j = 0; j <= n; j++){
//                for(int k = 0; k <= m; k++){
//                    System.err.printf("dp[%d][%d][%d] = %d\n", i, j, k, dp[i][j][k]);
//                }
//            }
//        }

        for(int i = 1; i <= m; i++){
            int ans = inf;
            for(int j = 0; j < 3; j++){
                ans = Math.min(ans, dp[j][n][i]);
            }
            out.append(ans).append(' ');
        }
    }
}
