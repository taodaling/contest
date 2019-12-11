package on2019_12.on2019_12_11_Codeforces_Round__531__Div__3_.F__Elongated_Matrix;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.math.Log2;
import template.utils.SequenceUtils;

public class FElongatedMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readInt();
            }
        }

        int[][] minDist = new int[n][n];
        SequenceUtils.deepFill(minDist, (int) 1e9);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    minDist[i][j] = Math.min(minDist[i][j], Math.abs(mat[i][k] - mat[j][k]));
                }
            }
        }
        int[][] minDistBetweenHeadAndTail = new int[n][n];
        SequenceUtils.deepFill(minDistBetweenHeadAndTail, (int) 1e9);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 1; k < m; k++) {
                    minDistBetweenHeadAndTail[i][j] = Math.min(minDistBetweenHeadAndTail[i][j], Math.abs(mat[i][k] - mat[j][k - 1]));
                }
            }
        }

        Log2 log2 = new Log2();
        BitOperator bo = new BitOperator();
        int[][][] dp = new int[1 << n][n][n];
        for (int i = 1; i < (1 << n); i++) {
            if (i == Integer.lowestOneBit(i)) {
                dp[i][log2.floorLog(i)][log2.floorLog(i)] = (int) 1e9;
                continue;
            }
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (bo.bitAt(i, j) == 0) {
                        continue;
                    }
                    for (int t = 0; t < n; t++) {
                        dp[i][j][k] = Math.max(dp[i][j][k],
                                Math.min(dp[bo.setBit(i, j, false)][t][k],
                                        minDist[j][t]));
                    }
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans = Math.max(ans, Math.min(dp[(1 << n) - 1][i][j], minDistBetweenHeadAndTail[j][i]));
            }
        }
        out.println(ans);
    }
}
