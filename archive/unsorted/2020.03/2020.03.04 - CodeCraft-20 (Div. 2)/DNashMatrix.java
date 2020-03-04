package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class DNashMatrix {
    char[][] ans;
    int[][][] mat;
    int n;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        mat = new int[n + 1][n + 1][2];
        ans = new char[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 0; k < 2; k++) {
                    mat[i][j][k] = in.readInt();
                }
            }
        }


        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (mat[i][j][0] != -1) {
                    continue;
                }
                for (int[] dir : dirs) {
                    int ni = i + dir[0];
                    int nj = j + dir[1];
                    if (check(ni, nj) && mat[ni][nj][0] == -1) {
                        ans[i][j] = get(i, j, ni, nj);
                        break;
                    }
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (mat[i][j][0] == i && mat[i][j][1] == j) {
                    dfs(i, j, i, j);
                }
            }
        }

        debug.debug("ans", ans);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (ans[i][j] == 0) {
                    out.println("INVALID");
                    return;
                }
            }
        }

        out.println("VALID");
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                out.append(ans[i][j]);
            }
            out.println();
        }
    }

    int[][] dirs = new int[][]{
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public char get(int i, int j, int ni, int nj) {
        if (i < ni) {
            return 'D';
        }
        if (i > ni) {
            return 'U';
        }
        if (j < nj) {
            return 'R';
        }
        if (j > nj) {
            return 'L';
        }
        return 'X';
    }

    public boolean check(int i, int j) {
        if (i <= 0 || i > n || j <= 0 || j > n) {
            return false;
        }
        return true;
    }

    public void dfs(int i, int j, int from, int to) {
        if (!check(i, j)) {
            return;
        }
        if (ans[i][j] != 0) {
            return;
        }
        if (mat[i][j][0] != mat[from][to][0] ||
                mat[i][j][1] != mat[from][to][1]) {
            return;
        }
        ans[i][j] = get(i, j, from, to);
        for (int[] dir : dirs) {
            int ni = dir[0] + i;
            int nj = dir[1] + j;
            dfs(ni, nj, i, j);
        }
    }

}
