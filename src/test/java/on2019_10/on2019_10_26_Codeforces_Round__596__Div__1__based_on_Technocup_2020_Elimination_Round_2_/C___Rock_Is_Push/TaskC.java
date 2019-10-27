package on2019_10.on2019_10_26_Codeforces_Round__596__Div__1__based_on_Technocup_2020_Elimination_Round_2_.C___Rock_Is_Push;



import template.SequenceUtils;
import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskC {
    int n;
    int m;
    int[][] left;
    int[][] top;
    int[][] h;
    int[][] v;
    int[][] hs;
    int[][] vs;
    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        char[][] grids = new char[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            in.readString(grids[i], 1);
        }
        if (n == 1 && m == 1) {
            out.println(1);
            return;
        }

        left = new int[n + 1][m + 1];
        top = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                left[i][j] = left[i][j - 1] + (grids[i][j] == 'R' ? 1 : 0);
            }
        }
        for (int j = 1; j <= m; j++) {
            for (int i = 1; i <= n; i++) {
                top[i][j] = top[i - 1][j] + (grids[i][j] == 'R' ? 1 : 0);
            }
        }

        h = new int[n + 1][m + 1];
        v = new int[n + 1][m + 1];
        hs = new int[n + 1][m + 1];
        vs = new int[n + 1][m + 1];

        SequenceUtils.fill(h, -1);
        SequenceUtils.fill(v, -1);
        SequenceUtils.fill(hs, -1);
        SequenceUtils.fill(vs, -1);
        h[n][m] = v[n][m] = 1;

        //h(2, 2);
        int ans1 = h(1, 1);
        int ans2 = v(1, 1);
        int ans = mod.plus(ans1, ans2);
        out.println(ans);
    }

    public int h(int i, int j) {
        if (h[i][j] == -1) {
            int stoneInRight = left[i][m] - left[i][j];
            int maxTo = m - stoneInRight;
            h[i][j] = mod.subtract(vs(i, j + 1), vs(i, maxTo + 1));
        }
        return h[i][j];
    }

    public int v(int i, int j) {
        if (v[i][j] == -1) {
            int stoneInBottom = top[n][j] - top[i][j];
            int maxTo = n - stoneInBottom;
            v[i][j] = mod.subtract(hs(i + 1, j), hs(maxTo + 1, j));
        }
        return v[i][j];
    }

    public int hs(int i, int j) {
        if (i > n) {
            return 0;
        }
        if (hs[i][j] == -1) {
            hs[i][j] = mod.plus(hs(i + 1, j), h(i, j));
        }
        return hs[i][j];
    }

    public int vs(int i, int j) {
        if (j > m) {
            return 0;
        }
        if (vs[i][j] == -1) {
            vs[i][j] = mod.plus(vs(i, j + 1), v(i, j));
        }
        return vs[i][j];
    }
}
