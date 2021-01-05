package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CCaninePoetry {
    char[] s = new char[(int) 1e5 + 1];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s, 1);
        int[][] dp = new int[1 << 2][n + 1];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        int mask = 3;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 1 << 2; j++) {
                for (int k = 0; k < 2; k++) {
                    if (k == 0) {
                        if (i - 1 > 0 && s[i - 1] == s[i + 1] && Bits.get(j, 1) == 0) {
                            continue;
                        }
                        if (i > 0 && s[i] == s[i + 1] && Bits.get(j, 0) == 0) {
                            continue;
                        }
                    }
                    int next = ((j << 1) | k) & mask;
                    dp[next][i + 1] = Math.min(dp[next][i + 1], dp[j][i] + k);
                }
            }
        }
        int ans = inf;
        for(int i = 0; i < 4; i++){
            ans = Math.min(ans, dp[i][n]);
        }
        out.println(ans);
    }
}
