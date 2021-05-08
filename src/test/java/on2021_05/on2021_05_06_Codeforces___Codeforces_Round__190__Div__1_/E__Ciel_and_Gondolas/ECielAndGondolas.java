package on2021_05.on2021_05_06_Codeforces___Codeforces_Round__190__Div__1_.E__Ciel_and_Gondolas;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ECielAndGondolas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        mat = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = in.ri();
                mat[i][j] += mat[i][j - 1];
            }
        }
        cost = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            int sum = 0;
            for (int j = i; j <= n; j++) {
                sum += ps(j, i, j);
                cost[i][j] = sum;
            }
        }

        prev = new int[n + 1];
        next = new int[n + 1];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int i = 0; i < k; i++) {
            Arrays.fill(next, inf);
            dac(1, n, 1, n);
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = prev[n];
        out.println(ans);
    }

    int[] prev;
    int[] next;
    int inf = (int) 1e9;

    void dac(int L, int R, int l, int r) {
        if (l > r) {
            return;
        }
        int m = (l + r) / 2;
        int best = inf;
        int bestIndex = L;
        for (int i = L; i <= R && i <= m; i++) {
            int cand = prev[i - 1] + cost[i][m];
            if (cand < best) {
                best = cand;
                bestIndex = i;
            }
        }
        next[m] = best;
        dac(L, bestIndex, l, m - 1);
        dac(bestIndex, R, m + 1, r);
    }

    public int ps(int i, int l, int r) {
        if (l > r) {
            return 0;
        }
        int ans = mat[i][r];
        if (l > 0) {
            ans -= mat[i][l - 1];
        }
        return ans;
    }

    int[][] mat;
    int[][] cost;
}

