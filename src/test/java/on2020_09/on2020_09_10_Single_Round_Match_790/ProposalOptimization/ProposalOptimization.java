package on2020_09.on2020_09_10_Single_Round_Match_790.ProposalOptimization;



import template.algo.DoubleBinarySearch;
import template.algo.DoubleFixRoundBinarySearch;
import template.algo.WQSBinarySearch;
import template.primitve.generated.datastructure.DoubleBinaryFunction;
import template.utils.SequenceUtils;

public class ProposalOptimization {
    double[][] mat;
    int[][] bestCost;
    double[][] dp;
    int R;
    int C;
    int K;
    int[] roses;
    int[] tulips;
    int[] costs;

    public double bestPath(int R, int C, int K, int[] roses, int[] tulips, int[] costs) {
        mat = new double[R][C];
        bestCost = new int[R][C];
        dp = new double[R][C];
        this.R = R;
        this.C = C;
        this.K = K;
        this.roses = roses;
        this.tulips = tulips;
        this.costs = costs;
        DoubleFixRoundBinarySearch dbs = new DoubleFixRoundBinarySearch(100) {
            @Override
            public boolean check(double mid) {
                return bestProfit(mid) <= 0;
            }
        };
        double ans = dbs.binarySearch(0, 1e9);

        return ans;
    }

    public double bestProfit(double mid) {
        WQSBinarySearch bs = new WQSBinarySearch() {
            @Override
            protected double getBest() {
                return best;
            }

            @Override
            protected int getTime() {
                return time;
            }

            double best;
            int time;

            @Override
            protected void check(double costPerOperation) {
                for (int i = 0; i < R; i++) {
                    for (int j = 0; j < C; j++) {
                        mat[i][j] = roses[i * C + j] - mid * tulips[i * C + j] - costPerOperation * costs[i * C + j];
                    }
                }
                SequenceUtils.deepFill(dp, -1e30);
                dp[0][0] = 0;
                for (int i = 0; i < R; i++) {
                    for (int j = 0; j < C; j++) {
                        if (i + 1 < R && dp[i + 1][j] < dp[i][j] + mat[i + 1][j]) {
                            dp[i + 1][j] = dp[i][j] + mat[i + 1][j];
                            bestCost[i + 1][j] = bestCost[i][j] + costs[(i + 1) * C + j];
                        }
                        if (j + 1 < C && dp[i][j] + mat[i][j + 1] > dp[i][j + 1]) {
                            dp[i][j + 1] = dp[i][j] + mat[i][j + 1];
                            bestCost[i][j + 1] = bestCost[i][j] + costs[i * C + j + 1];
                        }
                    }
                }

                best = dp[R - 1][C - 1];
                time = bestCost[R - 1][C - 1];
            }
        };
        double ans = bs.search(0, 1e9, 300, K, true);
        return ans;
    }
}
