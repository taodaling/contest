package on2021_07.on2021_07_13_Single_Round_Match_809.TruckUnion0;



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
//        ModMatrix mat = new ModMatrix((i, j) -> cost(i, j), N, N);
//        debug.debug("mat", mat);
        int inf = (int) 1e9;
        int best = inf;
        int[][] dp = new int[N - 1][1 << N - 1];
        for (int each = 1; each <= N - 1; each++) {
            if ((N - 1) % each != 0) {
                continue;
            }
            SequenceUtils.deepFill(dp, inf);
            for (int i = 0; i < N - 1; i++) {
                dp[i][1 << i] = 0;
            }
            for (int i = 1; i < 1 << N - 1; i++) {
                boolean cost = true;
                int bc = Integer.bitCount(i);
                if ((bc - 1) % each == 0) {
                    for (int j = 0; j < N - 1; j++) {
                        dp[j][i] += cost(0, j + 1);
                    }
                }
                if (bc % each == 0) {
                    cost = false;
                    for (int j = 0; j < N - 1; j++) {
                        dp[j][i] += cost(j + 1, 0);
                    }
                }
                for (int j = 0; j < N - 1; j++) {
                    if (Bits.get(i, j) == 1) {
                        continue;
                    }
                    for (int k = 0; k < N - 1; k++) {
                        dp[j][i | 1 << j] = Math.min(dp[j][i | 1 << j], dp[k][i] + (cost ? cost(k + 1, j + 1) : 0));
                    }
                }
            }
            for(int i = 0; i < N - 1; i++){
                best = Math.min(best, dp[i][(1 << N - 1) - 1]);
            }
        }

        return best;
    }
}
