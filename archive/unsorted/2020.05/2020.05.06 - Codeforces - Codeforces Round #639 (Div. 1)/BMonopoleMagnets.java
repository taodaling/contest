package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class BMonopoleMagnets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar();
            }
        }

        boolean valid = true;
        int emptyRow = 0;
        for (int i = 0; i < n; i++) {
            int left = -1;
            int right = -1;
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '#') {
                    if (left == -1) {
                        left = j;
                    }
                    right = j;
                }
            }

            if (left == -1) {
                emptyRow++;
                continue;
            }
            for (int j = left; j <= right; j++) {
                if (mat[i][j] != '#') {
                    valid = false;
                }
            }
        }

        int emptyCol = 0;
        for (int i = 0; i < m; i++) {
            int top = -1;
            int bot = -1;
            for (int j = 0; j < n; j++) {
                if (mat[j][i] == '#') {
                    if (top == -1) {
                        top = j;
                    }
                    bot = j;
                }
            }

            if (top == -1) {
                emptyCol++;
                continue;
            }
            for (int j = top; j <= bot; j++) {
                if (mat[j][i] != '#') {
                    valid = false;
                }
            }
        }

        if (emptyCol > 0 && emptyRow == 0 ||
                emptyRow > 0 && emptyCol == 0) {
            valid = false;
        }
        if (!valid) {
            out.println(-1);
            return;
        }

        DSU dsu = new DSU(n * m);
        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] d : dirs) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni < 0 || ni >= n || nj < 0 || nj >= m) {
                        continue;
                    }
                    if (mat[i][j] == '#' && mat[ni][nj] == '#') {
                        dsu.merge(id(i, j), id(ni, nj));
                    }
                }
            }
        }

        int cc = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int id = id(i, j);
                if (mat[i][j] == '#' && dsu.find(id) == id) {
                    cc++;
                }
            }
        }

        out.println(cc);
    }

    int n;
    int m;

    public int id(int i, int j) {
        return i * m + j;
    }
}
