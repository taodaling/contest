package on2020_05.on2020_05_22_Codeforces___Codeforces_Round__415__Div__1_.C__Find_a_car;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.ArrayIndex;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CFindACar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int x1 = in.readInt() - 1;
            int y1 = in.readInt() - 1;
            int x2 = in.readInt() - 1;
            int y2 = in.readInt() - 1;
            int k = in.readInt() - 1;

            int ans = solve(x1, x2, y1, y2, k);
            out.println(ans);
        }
    }

    Modular mod = new Modular(1e9 + 7);
    ArrayIndex ai = new ArrayIndex(31, 2, 2, 2, 2, 2);
    int[][] dp = new int[ai.totalSize()][2];
    int x1;
    int x2;
    int y1;
    int y2;
    int k;

    int[] end = new int[]{1, 0};

    public int[] dp(int i, int xFloor, int xCeil, int yFloor, int yCeil, int kCeil) {
        if (i < 0) {
            return end;
        }
        int index = ai.indexOf(i, xFloor, xCeil, yFloor, yCeil, kCeil);

        if (dp[index][0] == -1) {
            int xL = Bits.bitAt(x1, i);
            int xR = Bits.bitAt(x2, i);
            int yL = Bits.bitAt(y1, i);
            int yR = Bits.bitAt(y2, i);
            int ki = Bits.bitAt(k, i);

            dp[index][0] = dp[index][1] = 0;
            for (int a = 0; a < 2; a++) {
                for (int b = 0; b < 2; b++) {
                    int xor = a ^ b;
                    if (xFloor == 1 && a < xL
                            || xCeil == 1 && a > xR
                            || yFloor == 1 && b < yL
                            || yCeil == 1 && b > yR
                            || kCeil == 1 && xor > ki) {
                        continue;
                    }
                    int[] ans = dp(i - 1,
                            xFloor == 1 && a == xL ? 1 : 0,
                            xCeil == 1 && a == xR ? 1 : 0,
                            yFloor == 1 && b == yL ? 1 : 0,
                            yCeil == 1 && b == yR ? 1 : 0,
                            kCeil == 1 && ki == xor ? 1 : 0);

                    //cnt
                    dp[index][0] = mod.plus(dp[index][0], ans[0]);
                    dp[index][1] = mod.plus(dp[index][1], ans[1]);
                    dp[index][1] = mod.plus(dp[index][1], mod.mul(xor << i, ans[0]));
                }
            }
        }

        return dp[index];
    }

    int highest = 30;

    public int solve(int x1, int x2, int y1, int y2, int k) {
        SequenceUtils.deepFill(dp, -1);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.k = k;

        int[] ret = dp(highest, 1, 1, 1, 1, 1);
        int ans = mod.plus(ret[1], ret[0]);
        return ans;
    }
}
