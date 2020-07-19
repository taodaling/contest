package on2020_07.on2020_07_18_TopCoder_SRM__743.ExpectedSum;



import template.math.KahanSummation;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ExpectedSum {
    public double solve(int[] sequence, int[] probMinus) {
        int n = sequence.length;
        int half = sequence.length / 2;
        double[] probs = Arrays.stream(probMinus).mapToDouble(x -> x / 100D).toArray();
        double[][] left = solve(Arrays.copyOf(sequence, half), Arrays.copyOf(probs, half), false);
        double[][] right = solve(Arrays.copyOfRange(sequence, half, n),
                Arrays.copyOfRange(probs, half, n), true);
        double[][] leftX = mulX(left);
        double[][] rightX = mulX(right);

        double[][] leftPs = prefixSum(left);
        double[][] rightPs = prefixSum(right);
        double[][] leftXPs = prefixSum(leftX);
        double[][] rightXPs = prefixSum(rightX);
        //(x, y), (a, b)
        //use y
        KahanSummation exp = new KahanSummation();
        for (int x = 0; x < limit; x++) {
            for (int y = x; y < limit; y++) {
                //x + a <= y
                //a <= y - x
                double p = region(rightPs, 0, y - x, 0, y);
                p *= left[x][y];
                exp.add(p * y);
            }
        }

        for (int a = 0; a < limit; a++) {
            for (int b = a; b < limit; b++) {
                //x + a <= y
                //a <= y - x
                double p = region(leftPs, 0, b - a, 0, b - 1);
                p *= right[a][b];
                exp.add(p * b);
            }
        }

        for (int x = 0; x < limit; x++) {
            for (int y = 0; y < limit; y++) {
                //y >= b
                //x + a > y
                //a > y - x
                double p = region(rightPs, y - x + 1, limit - 1, 0, y);
                p *= left[x][y];
                double sum = region(rightXPs, y - x + 1, limit - 1, 0, y);
                sum *= left[x][y];
                exp.add(p * x + sum);
            }
        }

        for (int a = 0; a < limit; a++) {
            for (int b = 0; b < limit; b++) {
                //y < b
                //x + a > y
                //x > b - a
                double p = region(leftPs, b - a + 1, limit - 1, 0, b - 1);
                p *= right[a][b];
                double sum = region(leftXPs, b - a + 1, limit - 1, 0, b - 1);
                sum *= right[a][b];
                exp.add(p * a + sum);
            }
        }

        return exp.sum();
    }

    int limit = 50 * 25 + 1;

    public double[][] prefixSum(double[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        double[][] ans = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = mat[i][j];
                if (j > 0) {
                    ans[i][j] += ans[i][j - 1];
                }
            }
            if (i > 0) {
                for (int j = 0; j < m; j++) {
                    ans[i][j] += ans[i - 1][j];
                }
            }
        }
        return ans;
    }

    public double[][] mulX(double[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        double[][] ans = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = mat[i][j] * i;
            }
        }
        return ans;
    }

    public double region(double[][] ps, int r1, int r2, int c1, int c2) {
        r1 = Math.max(0, r1);
        c1 = Math.max(0, c1);
        if (r1 > r2 || c1 > c2) {
            return 0;
        }
        double ans = ps[r2][c2];
        if (r1 > 0) {
            ans -= ps[r1 - 1][c2];
        }
        if (c1 > 0) {
            ans -= ps[r2][c1 - 1];
        }
        if (r1 > 0 && c1 > 0) {
            ans += ps[r1 - 1][c1 - 1];
        }
        return ans;
    }

    public double[][] solve(int[] seq, double[] probs, boolean reverse) {
        if (reverse) {
            SequenceUtils.reverse(seq);
            SequenceUtils.reverse(probs);
        }
        this.seq = seq;
        this.probs = probs;
        this.ans = new double[limit][limit];
        dfs(0, 0, 1, 0);
        return ans;
    }

    double[][] ans;
    int[] seq;
    double[] probs;

    public void dfs(int maxDp, int maxSuf, double prob, int k) {
        if (k == seq.length) {
            ans[maxSuf][maxDp] += prob;
            return;
        }
        //minus
        int maxSufExt = Math.max(maxSuf - seq[k], 0);
        dfs(Math.max(maxDp, maxSufExt), maxSufExt, prob * probs[k], k + 1);
        maxSufExt = maxSuf + seq[k];
        dfs(Math.max(maxDp, maxSufExt), maxSufExt, prob * (1 - probs[k]), k + 1);
    }
}
