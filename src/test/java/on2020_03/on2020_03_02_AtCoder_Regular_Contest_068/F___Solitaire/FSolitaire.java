package on2020_03.on2020_03_02_AtCoder_Regular_Contest_068.F___Solitaire;



import template.datastructure.ModPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class FSolitaire {
    Debug debug = new Debug(true);
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[][] dp = new int[k][n + 2];
        dp[0][n + 1] = 1;
        for (int i = 1; i < k; i++) {
            ModPreSum ps = new ModPreSum(dp[i - 1], mod);
            for (int j = 1; j <= n; j++) {
                dp[i][j] = ps.suffix(j);
                if (n - j + 1 < i) {
                    dp[i][j] = 0;
                }
            }
        }

        int sum = new ModPreSum(dp[k - 1], mod).suffix(2);
        int way = k == n ? 1 : power.pow(2, n - k - 1);
        int ans = mod.mul(sum, way);
        out.println(ans);
    }
}
