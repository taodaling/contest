package on2021_09.on2021_09_22_Luogu.P3350__ZJOI2016____;



import template.graph.WeightedGridBatchShortestPath;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class P3350ZJOI2016 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] DIR4 = new int[][]{
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };
        long[][][] adj = new long[4][n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m - 1; j++) {
                adj[1][i][j + 1] = adj[3][i][j] = in.ri();
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m; j++) {
                adj[2][i + 1][j] = adj[0][i][j] = in.ri();
            }
        }
        int q = in.ri();
        int[][] qs = new int[4][q];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < 4; j++) {
                qs[j][i] = in.ri() - 1;
            }
        }
        boolean[][] ok = new boolean[n][m];
        SequenceUtils.deepFill(ok, true);
        WeightedGridBatchShortestPath gbsp = new WeightedGridBatchShortestPath(ok, DIR4, adj);
        long[] ans = gbsp.query(qs[0], qs[1], qs[2], qs[3]);
        for (long x : ans) {
            out.println(x);
        }
    }
}
