package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.PalindromeAutomaton;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BOnceAgain {
    int inf = (int) 1e9 + 10;

    public int[][] mul(int[][] a, int[][] b) {
        int n = a.length;
        int[][] ans = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans[i][j] = -inf;
                for (int k = 0; k < n; k++) {
                    ans[i][j] = Math.max(ans[i][j], a[i][k] + b[k][j]);
                }
            }
        }

        return ans;
    }

    public int[][] identity(int n) {
        int[][] ans = new int[n][n];
        SequenceUtils.deepFill(ans, -inf);
        for (int i = 0; i < n; i++) {
            ans[i][i] = 0;
        }
        return ans;
    }

    public int[][] pow(int[][] x, int m) {
        if (m == 0) {
            int n = x.length;
            return identity(n);
        }
        int[][] ans = pow(x, m / 2);
        ans = mul(ans, ans);
        if(m % 2 == 1){
            ans = mul(ans, x);
        }
        return ans;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int T = in.ri();
        int[] v = in.ri(n);
        IntegerArrayList all = new IntegerArrayList(v);
        all.unique();
        for (int i = 0; i < n; i++) {
            v[i] = all.binarySearch(v[i]);
        }
        int m = all.size();
        int[][] prev = identity(m);
        int[][] next = new int[m][m];
        for (int x : v) {
            SequenceUtils.deepFill(next, -inf);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    next[i][j] = Math.max(next[i][j], prev[i][j]);
                    if (x >= j) {
                        next[i][x] = Math.max(next[i][x], prev[i][j] + 1);
                    }
                }
            }
            int[][] tmp = prev;
            prev = next;
            next = tmp;

            debug.debug("x", x);
            debug.debug("prev", prev);
        }

        debug.debug("prev", prev);
        int[][] ans = pow(prev, T);
        int max = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < m; j++){
                max = Math.max(max, ans[i][j]);
            }
        }
        out.println(max);
    }
}
