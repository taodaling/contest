package on2020_08.on2020_08_27_Codeforces___Codeforces_Round__366__Div__1_.B__Ant_Man;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BAntMan {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s = in.readInt();
        int e = in.readInt();

        int[] x = new int[n];
        //from left
        int[] a = new int[n];
        //from right
        int[] b = new int[n];
        //to left
        int[] c = new int[n];
        //to right
        int[] d = new int[n];
        in.populate(x);
        in.populate(a);
        in.populate(b);
        in.populate(c);
        in.populate(d);

        long[][] dp = new long[n + 1][n + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, inf);


        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int indegSum = i;
            int outdegSum = i;
            if (i >= s) {
                indegSum--;
            }
            if (i >= e) {
                outdegSum--;
            }
            for (int j = 0; j <= n; j++) {
                int indeg = j;
                int outdeg = outdegSum - (indegSum - j);
                if (i > 0 && indeg == 0 && outdeg <= 0) {
                    continue;
                }
                if (outdeg < 0) {
                    continue;
                }
                if (i >= e && i >= s && (indeg == 0 || outdeg == 0)) {
                    continue;
                }

                if (i + 1 == s) {
                    //add new
                    dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][j] + d[i] - x[i]);

                    //link
                    if (j > 0) {
                        dp[i + 1][j - 1] = Math.min(dp[i + 1][j - 1], dp[i][j] + c[i] + x[i]);
                    }

                    continue;
                }

                if (i + 1 == e) {
                    //add new
                    if (j + 1 <= n) {
                        dp[i + 1][j + 1] = Math.min(dp[i + 1][j + 1], dp[i][j] + b[i] - x[i]);
                    }
                    //link
                    if (outdeg > 0) {
                        dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][j] + a[i] + x[i]);
                    }
                    continue;
                }

                //add new
                if (j + 1 <= n) {
                    dp[i + 1][j + 1] = Math.min(dp[i + 1][j + 1], dp[i][j] + b[i] + d[i] - 2L * x[i]);
                }
                //to left from right
                if (j > 0 && indeg > 0) {
                    dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][j] + c[i] + b[i]);
                }
                //to right from left
                if (outdeg > 0) {
                    dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][j] + d[i] + a[i]);
                }
                //concatenate
                if (j > 0 && indeg > 0 && outdeg > 0) {
                    dp[i + 1][j - 1] = Math.min(dp[i + 1][j - 1], dp[i][j] + a[i] + c[i] + 2L * x[i]);
                }
            }
        }

        long ans = dp[n][0];
        out.println(ans);
    }
}
