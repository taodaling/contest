package on2021_07.on2021_07_13_Single_Round_Match_809.TruckUnion;



import template.binary.FastBitCount2;
import template.binary.Bits;
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
        int[][] dp = f;
        SequenceUtils.deepFill(dp, inf);
        for (int i = 0; i < N; i++) {
            if (i > 0 && (N - 1) % i == 0) {
                dp[i][0] = 0;
            }
        }
        int mask = (1 << N - 1) - 1;
        for (int j = 0; j < 1 << N - 1; j++) {
            int set = mask ^ j;
            int subset = set + 1;
            while (subset > 0) {
                subset = (subset - 1) & set;
                int size = FastBitCount2.count(subset);
                dp[size][subset | j] = Math.min(dp[size][subset | j], dp[size][j] + minCost[subset]);
            }
        }
        for(int i = 0; i < N; i++){
            best = Math.min(best, dp[i][mask]);
        }
        return best;
    }
}
