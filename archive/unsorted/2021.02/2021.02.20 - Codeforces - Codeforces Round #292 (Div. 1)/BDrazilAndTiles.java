package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.GridUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;

public class BDrazilAndTiles {
    char[][] mat;

    public void draw(int i, int j, int a, int b) {
        if (i != a) {
            if (i > a) {
                mat[i][j] = 'v';
                mat[a][b] = '^';
            } else {
                mat[i][j] = '^';
                mat[a][b] = 'v';
            }
        } else {
            if (j > b) {
                mat[i][j] = '>';
                mat[a][b] = '<';
            } else {
                mat[i][j] = '<';
                mat[a][b] = '>';
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
        }
        int[][] deg = new int[n][m];
        Deque<int[]> dq = new ArrayDeque<>(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '*') {
                    continue;
                }
                for (int[] d : GridUtils.DIRS) {
                    int ni = d[0] + i;
                    int nj = d[1] + j;
                    if (!valid(ni, nj) || mat[ni][nj] == '*') {
                        continue;
                    }
                    deg[i][j]++;
                }
                if (deg[i][j] == 1) {
                    dq.add(new int[]{i, j});
                }
            }
        }

        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            int i = head[0];
            int j = head[1];
            if (mat[i][j] != '.') {
                continue;
            }
            for (int[] d : GridUtils.DIRS) {
                int ni = d[0] + i;
                int nj = d[1] + j;
                if (!valid(ni, nj) || mat[ni][nj] != '.') {
                    continue;
                }
                draw(i, j, ni, nj);
                for (int[] dd : GridUtils.DIRS) {
                    int nni = dd[0] + ni;
                    int nnj = dd[1] + nj;
                    if (!valid(nni, nnj) || mat[nni][nnj] != '.') {
                        continue;
                    }
                    deg[nni][nnj]--;
                    if (deg[nni][nnj] == 1) {
                        dq.addLast(new int[]{nni, nnj});
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '.') {
                    out.println("Not unique");
                    return;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            out.println(mat[i]);
        }
    }

    int n;
    int m;

    public boolean valid(int i, int j) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }
}
