package on2021_04.on2021_04_20_Codeforces___Codeforces_Round__204__Div__1_.C__Jeff_and_Brackets;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CJeffAndBrackets {
    long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        long[][] prev = new long[2 * n][2 * n];
        SequenceUtils.deepFill(prev, inf);
        for (int i = 0; i < 2 * n; i++) {
            prev[i][i] = 0;
        }
        long[][] next = new long[2 * n][2 * n];
        for (int i = 0; i < n; i++) {
            SequenceUtils.deepFill(next, inf);
            for (int j = 0; j < 2 * n; j++) {
                for (int k = 0; k < 2 * n; k++) {
                    //(
                    if (k + 1 < 2 * n) {
                        next[j][k + 1] = Math.min(next[j][k + 1], prev[j][k] + a[i]);
                    }
                    //)
                    if (k > 0) {
                        next[j][k - 1] = Math.min(next[j][k - 1], prev[j][k] + b[i]);
                    }
                }
            }

            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        long[][] res = pow(prev, m);
        long ans = res[0][0];
        out.println(ans);
    }

    long[][] mul(long[][] a, long[][] b) {
        int n = a.length;
        long[][] c = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long best = inf;
                for (int k = 0; k < n; k++) {
                    best = Math.min(best, a[i][k] + b[k][j]);
                }
                c[i][j] = best;
            }
        }
        return c;
    }

    long[][] pow(long[][] x, int m){
        if(m == 0){
            int n = x.length;
            long[][] ans = new long[n][n];
            SequenceUtils.deepFill(ans, inf);
            for(int i = 0; i < n; i++){
                ans[i][i] = 0;
            }
            return ans;
        }
        long[][] ans = pow(x, m / 2);
        ans = mul(ans, ans);
        if(m % 2 == 1){
            ans = mul(ans, x);
        }
        return ans;
    }
}
