package on2021_07.on2021_07_18_TopCoder_SRM__809.TruckUnion;



import template.binary.Bits;
import template.binary.FixedSizeSubsetGenerator;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TruckUnion {
    int[] C;
    int N;

    public int cost(int i, int j) {
        return C[i * N + j];
    }


//    Debug debug = new Debug(true);

    public int optimize(int N, int[] C) {
        this.N = N;
        this.C = C;
        int[][] f = new int[N][1 << N - 1];
//        ModMatrix mat = new ModMatrix((i, j) -> cost(i, j), N, N);
//        debug.debug("mat", mat);
        int inf = (int) 1e9;
        SequenceUtils.deepFill(f, inf);
        f[0][0] = 0;
        for (int i = 0; i < 1 << N - 1; i++) {
            for (int j = 0; j < N; j++) {
                if (f[j][i] == inf) {
                    continue;
                }
                for (int k = 0; k < N - 1; k++) {
                    if (Bits.get(i, k) == 1) {
                        continue;
                    }
                    f[k + 1][i | (1 << k)] = Math.min(f[k + 1][i | (1 << k)], f[j][i] + cost(j, k + 1));
                }
            }
        }
        int[] minCost = new int[1 << N - 1];
        Arrays.fill(minCost, inf);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 1 << N - 1; j++) {
                int to = j;
                minCost[to] = Math.min(minCost[to], f[i][j] + cost(i, 0));
            }
        }
        int best = inf;
        int mask = (1 << N - 1) - 1;
        int[] dp = new int[1 << N - 1];
        FixedSizeSubsetGenerator sg = new FixedSizeSubsetGenerator();
        for (int i = 1; i <= N - 1; i++) {
//            debug.debug("i", i);
            if ((N - 1) % i != 0) {
                continue;
            }
            dp[0] = 0;
            for (int j = 1; j < 1 << N - 1; j++) {
//                debug.debug("j", j);
                sg.init(j, i);
                dp[j] = inf;
                while (sg.next()) {
                    int s = sg.mask();
                    dp[j] = Math.min(dp[j], minCost[s] + dp[j - s]);
                }
            }
            best = Math.min(best, dp[mask]);
        }
        return best;
    }
//    Debug debug = new Debug(false);
}
