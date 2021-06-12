package on2021_06.on2021_06_08_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.G__LCS_8;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class GLCS8 {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        int charset = 'Z' - 'A' + 1;
        int[][] next = new int[charset][n + 1];
        for (int i = 0; i < charset; i++) {
            next[i][n] = n + 1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < charset; j++) {
                next[i][j] = next[i][j + 1];
            }
            next[s[i] - 'A'][i] = i + 1;
        }
        int k = in.ri();
        int zero = k;
        long[][][] dp = new long[k + 1][zero + k + 1][n + 1];
        dp[0][zero][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= zero + k; j++) {
                int aj = j - zero + i;
                for (int t = 0; t <= k; t++) {
                    dp[t][j][i] %= mod;
                    if(dp[t][j][i] == 0){
                        continue;
                    }
                    if (aj <= n) {
                        //match
                        for (int x = 0; x < charset; x++) {
                            if (next[aj][x] == n + 1) {
                                continue;
                            }
                            int naj = next[aj][x];
                            int offset = naj - (i + 1) + zero;
                            if (offset >= 0 && offset <= zero + k) {
                                dp[t][offset][i + 1] += dp[t][j][i];
                            }
                        }
                    }
                    //discard

                }
            }
        }
    }
}
