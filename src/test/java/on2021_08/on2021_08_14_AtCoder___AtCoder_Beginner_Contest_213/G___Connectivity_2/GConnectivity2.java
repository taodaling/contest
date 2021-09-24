package on2021_08.on2021_08_14_AtCoder___AtCoder_Beginner_Contest_213.G___Connectivity_2;



import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.utils.Debug;

public class GConnectivity2 {
    int mod = 998244353;
    FastPow2 fp2 = new FastPow2(2, mod);
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            adj[a][b] = adj[b][a] = true;
        }
        int[] E = new int[1 << n];
        for (int i = 1; i < 1 << n; i++) {
            int lowest = Integer.lowestOneBit(i);
            int log = Log2.floorLog(lowest);
            int sub = i - lowest;
            E[i] = E[sub];
            for (int j = 0; j < n; j++) {
                if (Bits.get(sub, j) == 1 && adj[log][j]) {
                    E[i]++;
                }
            }
        }
        long[] ans = new long[n];
        long[] H = new long[1 << n];
        int mask = H.length - 1;
        for (int i = 1; i < 1 << n; i++) {
            if (Bits.get(i, 0) == 0) {
                continue;
            }
            long contrib = fp2.pow(E[i]);
            int subset = i;
            while (subset > 0) {
                subset = (subset - 1) & i;
                if (Bits.get(subset, 0) == 0) {
                    continue;
                }
                contrib -= H[subset] * fp2.pow(E[i - subset]) % mod;
            }
            H[i] = DigitUtils.mod(contrib, mod);
            if(H[i] != 0) {
                long total = H[i] * fp2.pow(E[mask - i]) % mod;
                for (int j = 0; j < n; j++) {
                    if (Bits.get(i, j) == 1) {
                        ans[j] += total;
                    }
                }
            }
        }

        for(int i = 1; i < n; i++){
            ans[i] %= mod;
            out.println(ans[i]);
        }

    }
}
