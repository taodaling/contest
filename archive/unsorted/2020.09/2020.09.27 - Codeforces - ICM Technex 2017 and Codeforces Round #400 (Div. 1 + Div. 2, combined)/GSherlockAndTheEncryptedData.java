package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class GSherlockAndTheEncryptedData {
    int max;
    long[][][][] dp = new long[16][2][2][2];
    int[] l = new int[16];
    int[] r = new int[16];

    public long dp(int i, int added, int ceil, int floor) {
        if (i < 0) {
            return added;
        }
        if (dp[i][added][ceil][floor] == -1) {
            long ans = 0;
            for (int j = 0; j <= max; j++) {
                if (j < l[i] && floor == 1) {
                    continue;
                }
                if (j > r[i] && ceil == 1) {
                    continue;
                }
                if ((max >> 2) == i && ((1 << (max & 3)) & j) == 0) {
                    continue;
                }
                ans += dp(i - 1, added == 1 || j == max ? 1 : 0,
                        ceil == 1 && j == r[i] ? 1 : 0,
                        floor == 1 && j == l[i] ? 1 : 0);
            }
            dp[i][added][ceil][floor] = ans;
        }
        return dp[i][added][ceil][floor];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long left = Long.parseLong(in.readString(), 16);
        long right = Long.parseLong(in.readString(), 16);
        for (int i = 0; i < 16; i++) {
            l[i] = (int) (left & 15);
            left >>>= 4;
            r[i] = (int) (right & 15);
            right >>>= 4;
        }

        long ans = 0;
        for (int i = 0; i < 16; i++) {
            SequenceUtils.deepFill(dp, -1L);
            max = i;
            ans += dp(15, 0, 1, 1);
        }

        out.println(ans);
    }
}
