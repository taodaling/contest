package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class KindsOfPeople {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() - '0';
            }
        }

        DSU dsu = new DSU(n * m);
        int[][] dirs = new int[][]{
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] d : dirs) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni < 0 || ni >= n || nj < 0 || nj >= m || mat[i][j] != mat[ni][nj]) {
                        continue;
                    }
                    dsu.merge(idOf(i, j), idOf(ni, nj));
                }
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int x1 = in.readInt() - 1;
            int y1 = in.readInt() - 1;
            int x2 = in.readInt() - 1;
            int y2 = in.readInt() - 1;
            if (dsu.find(idOf(x1, y1)) == dsu.find(idOf(x2, y2))) {
                if (mat[x1][y1] == 0) {
                    out.println("binary");
                } else {
                    out.println("decimal");
                }
            } else {
                out.println("neither");
            }
        }
    }

    int m;

    int idOf(int i, int j) {
        return i * m + j;
    }
}
