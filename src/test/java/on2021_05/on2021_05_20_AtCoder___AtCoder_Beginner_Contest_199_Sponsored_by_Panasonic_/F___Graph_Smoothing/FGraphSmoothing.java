package on2021_05.on2021_05_20_AtCoder___AtCoder_Beginner_Contest_199_Sponsored_by_Panasonic_.F___Graph_Smoothing;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModMatrix;
import template.math.Power;
import template.utils.Debug;

public class FGraphSmoothing {
    int mod = (int) 1e9 + 7;
    Power power = new Power(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[i][j] = in.ri() - 1;
            }
        }

        int inv2 = (mod + 1) / 2;
        int[] allAdd = new int[n];
        long[][] mat = new long[n][n];
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            allAdd[u]--;
            allAdd[v]--;
            mat[u][u] += inv2;
            mat[u][v] += inv2;
            mat[v][v] += inv2;
            mat[v][u] += inv2;
        }
        for (int i = 0; i < n; i++) {
            mat[i][i] += allAdd[i] + m;
        }

        ModMatrix A = new ModMatrix((i, j) -> DigitUtils.mod(mat[i][j], mod), n, n);
        ModMatrix v0 = new ModMatrix(a.clone(), 1);
        debug.debug("A", A);
        debug.debug("v0", v0);
        ModMatrix vk = ModMatrix.mul(ModMatrix.pow(A, k, mod), v0, mod);
        int div = power.inverse(power.pow(m, k));
        debug.debug("div", div);
        debug.debug("vk", vk);
        for(int i = 0; i < n; i++){
            long ans = vk.get(i, 0);
            ans = ans * div % mod;
            out.println(ans);
        }
    }

    Debug debug = new Debug(false);
}
