package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerFunction;
import template.utils.SequenceUtils;

public class UOJ0002 {
    int[] cast = new int[]{0, (1 << 30) - 1};
    int[][] dp = new int[32][2];
    int m;

    int dp(int i, int ceil) {
        if (i == -1) {
            return 0;
        }
        if (dp[i][ceil] == -1) {
            for (int j = 0; j < 2; j++) {
                if (ceil == 1 && Bits.bitAt(m, i) < j) {
                    continue;
                }
                dp[i][ceil] = Math.max(dp[i][ceil], (cast[j] & (1 << i)) + dp(i - 1, ceil == 1 && Bits.bitAt(m, i) == j ? 1 : 0));
            }
        }
        return dp[i][ceil];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        m = in.readInt();
        char[] cmd = new char[3];
        for (int i = 0; i < n; i++) {
            in.readString(cmd, 0);
            int x = in.readInt();
            if (cmd[0] == 'A') {
                cast[0] &= x;
                cast[1] &= x;
            }
            if (cmd[0] == 'O') {
                cast[0] |= x;
                cast[1] |= x;
            }
            if (cmd[0] == 'X') {
                cast[0] ^= x;
                cast[1] ^= x;
            }
        }

        SequenceUtils.deepFill(dp, -1);
        int ans = dp(30, 1);
        out.println(ans);
    }
}