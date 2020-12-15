package on2020_12.on2020_12_13_Single_Round_Match_795.AtLeastKDays;



import template.utils.SequenceUtils;

public class AtLeastKDays {
    public long sumOfMinCosts(String[] costs, int minDays) {
        int n = costs.length;
        long[][] e = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    e[i][j] = inf;
                } else {
                    e[i][j] = costs[i].charAt(j) - '0';
                }
            }
        }
        long[][] ans = trans(e, minDays);

        long[][] cand = new long[n][n];
        SequenceUtils.deepFill(cand, inf);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    cand[j][k] = Math.min(cand[j][k], ans[j][k]);
                }
            }
            ans = merge(ans, e);
        }
        long sum = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                sum += cand[i][j];
            }
        }
        return sum;
    }

    long inf = (long) 1e18;

    public long[][] merge(long[][] a, long[][] b) {
        int n = a.length;
        long[][] ans = new long[n][n];
        SequenceUtils.deepFill(ans, inf);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] = Math.min(ans[i][j], a[i][k] + b[k][j]);
                }
            }
        }
        return ans;
    }

    public long[][] trans(long[][] e, int n) {
        if (n == 0) {
            int m = e.length;
            long[][] ans = new long[m][m];
            SequenceUtils.deepFill(ans, inf);
            for (int i = 0; i < m; i++) {
                ans[i][i] = 0;
            }
            return ans;
        }
        long[][] ans = trans(e, n / 2);
        ans = merge(ans, ans);
        if (n % 2 == 1) {
            ans = merge(ans, e);
        }
        return ans;
    }
}
