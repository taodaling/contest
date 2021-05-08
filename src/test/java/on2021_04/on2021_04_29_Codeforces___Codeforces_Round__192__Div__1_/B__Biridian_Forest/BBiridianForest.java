package on2021_04.on2021_04_29_Codeforces___Codeforces_Round__192__Div__1_.B__Biridian_Forest;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.GridUtils;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class BBiridianForest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
        }
        int[] exit = null;
        int[] me = null;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 'E') {
                    exit = new int[]{i, j};
                }
                if (mat[i][j] == 'S') {
                    me = new int[]{i, j};
                }
            }
        }

        int[][] dists = new int[n][m];
        SequenceUtils.deepFill(dists, (int) 1e9);
        dists[exit[0]][exit[1]] = 0;
        Deque<int[]> dq = new ArrayDeque<>(n * m);
        dq.addLast(exit);
        while (!dq.isEmpty()) {
            int[] pos = dq.removeFirst();
            int x = pos[0];
            int y = pos[1];
            for (int[] d : GridUtils.DIRS) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m || dists[nx][ny] <= dists[x][y] + 1 ||
                        mat[nx][ny] == 'T') {
                    continue;
                }
                dists[nx][ny] = dists[x][y] + 1;
                dq.addLast(new int[]{nx, ny});
            }
        }

        int ans = 0;
        int threshold = dists[me[0]][me[1]];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (dists[i][j] <= threshold && Character.isDigit(mat[i][j])) {
                    ans += mat[i][j] - '0';
                }
            }
        }
        out.println(ans);
    }
}
