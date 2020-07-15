package on2020_07.on2020_07_11_AtCoder___ExaWizards_2019.D___Modulo_Operations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DModuloOperations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] s = new int[n];
        in.populate(s);
        Arrays.sort(s);
        SequenceUtils.reverse(s);
        int[][] dp = new int[n + 1][x + 1];
        dp[0][x] = 1;
        Modular mod = new Modular(1e9 + 7);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= x; j++) {
                int way = dp[i][j];
                if (way == 0) {
                    continue;
                }
                //add into
                dp[i + 1][j % s[i]] = mod.plus(dp[i + 1][j % s[i]], way);
                //not add into
                int follow = n - i - 1;
                if (follow > 0) {
                    dp[i + 1][j] = mod.plus(dp[i + 1][j], mod.mul(way, follow));
                }
            }
        }

        int ans = 0;
        for (int i = 0; i <= x; i++) {
            ans = mod.plus(ans, mod.mul(dp[n][i], i));
        }

        out.println(ans);
    }
}
