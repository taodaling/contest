package on2020_07.on2020_07_07_AtCoder___AtCoder_Grand_Contest_024.E___Sequence_Growing_Hard;



import com.sun.org.apache.xpath.internal.operations.Mod;
import template.graph.HeavyLightDecompose;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.SequenceUtils;

public class ESequenceGrowingHard {
    int[][] dp;
    int[][] ps;
    Modular mod;

    public int ps(int j, int k) {
        if (k < 0) {
            return 0;
        }
        if (ps[j][k] == -1) {
            int ans = ps(j, k - 1);
            ans = mod.plus(ans, dp(j, k));
            ps[j][k] = ans;
        }
        return ps[j][k];
    }

    public int dp(int j, int k) {
        if (j == 0) {
            return k == 1 ? 1 : 0;
        }
        if (dp[j][k] == -1) {
            dp[j][k] = mod.mul(k, ps(j - 1, k));
        }
        return dp[j][k];
    }

    int[][] f;


    public int f(int i, int j) {
        if(i < 1){
            return j == 0 ? 1 : 0;
        }
        if (f[i][j] == -1) {
            int ans = 0;
            for (int k = 0; k <= j; k++) {
                ans = mod.plus(ans, mod.mul(f(i - 1, k), ps(j - k, k + 1)));
            }
            f[i][j] = ans;
        }
        return f[i][j];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        mod = new Modular(in.readInt());

        dp = new int[n + 1][n + 2];
        ps = new int[n + 1][n + 2];
        f = new int[k + 1][n + 1];
        SequenceUtils.deepFill(dp, -1);
        SequenceUtils.deepFill(ps, -1);
        SequenceUtils.deepFill(f, -1);

        int ans = f(k, n);
        out.println(ans);
    }
}
