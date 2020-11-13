package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class MovingRobots {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        double[][][][] dp = new double[8][8][][];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                dp[i][j] = dp(i, j, k);
            }
        }
        double exp = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double prod = 1;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        prod *= 1 - dp[x][y][i][j];
                    }
                }
                exp += prod;
            }
        }
        out.printf("%.6f", exp);
    }

    int[][] dirs = new int[][]{
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public boolean valid(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public double[][] dp(int x, int y, int k) {
        double[][] prev = new double[8][8];
        double[][] next = new double[8][8];
        prev[x][y] = 1;
        for (int i = 0; i < k; i++) {
            SequenceUtils.deepFill(next, 0D);
            for (int j = 0; j < 8; j++) {
                for (int t = 0; t < 8; t++) {
                    int way = 0;
                    for (int[] d : dirs) {
                        int nx = j + d[0];
                        int ny = t + d[1];
                        if (valid(nx, ny)) {
                            way++;
                        }
                    }
                    double p = prev[j][t] / way;
                    for (int[] d : dirs) {
                        int nx = j + d[0];
                        int ny = t + d[1];
                        if (valid(nx, ny)) {
                            next[nx][ny] += p;
                        }
                    }
                }
            }
            double[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        return prev;
    }
}
