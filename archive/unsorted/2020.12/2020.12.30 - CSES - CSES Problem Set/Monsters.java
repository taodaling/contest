package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.SequenceUtils;

public class Monsters {
    int[][] dirs = new int[][]{
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1}
    };
    char[] way = "DRUL".toCharArray();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }
        int inf = (int) 1e9;
        int[][] dists1 = new int[n][m];
        int[][] dists2 = new int[n][m];
        SequenceUtils.deepFill(dists1, inf);
        SequenceUtils.deepFill(dists2, inf);
        LongDeque dq1 = new LongDequeImpl(n * m);
        LongDeque dq2 = new LongDequeImpl(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 'A') {
                    dists1[i][j] = 0;
                    dq1.addLast(DigitUtils.asLong(i, j));
                }
                if (mat[i][j] == 'M') {
                    dists2[i][j] = 0;
                    dq2.addLast(DigitUtils.asLong(i, j));
                }
            }
        }

        bfs(mat, dists1, dq1);
        bfs(mat, dists2, dq2);
        int[] pos = null;
        for (int i = 0; i < n; i++) {
            if (dists1[i][0] < dists2[i][0]) {
                pos = new int[]{i, 0};
                break;
            }
            if (dists1[i][m - 1] < dists2[i][m - 1]) {
                pos = new int[]{i, m - 1};
                break;
            }
        }
        for(int i = 0; i < m; i++){
            if (dists1[0][i] < dists2[0][i]) {
                pos = new int[]{0, i};
                break;
            }
            if (dists1[n - 1][i] < dists2[n - 1][i]) {
                pos = new int[]{n - 1, i};
                break;
            }
        }
        if (pos == null) {
            out.println("NO");
            return;
        }
        out.println("YES");
        StringBuilder ans = new StringBuilder(n * m);
        int x = pos[0];
        int y = pos[1];
        while (dists1[x][y] != 0) {
            for (int i = 0; i < 4; i++) {
                int nx = x - dirs[i][0];
                int ny = y - dirs[i][1];
                if (nx < 0 || ny < 0 || nx >= n || ny >= m ||
                        dists1[nx][ny] + 1 != dists1[x][y]) {
                    continue;
                }
                ans.append(way[i]);
                x = nx;
                y = ny;
                break;
            }
        }
        out.println(ans.length());
        out.println(ans.reverse());
    }

    public void bfs(char[][] mat, int[][] dists, LongDeque dq) {
        int n = mat.length;
        int m = mat[0].length;
        while (!dq.isEmpty()) {
            int x = DigitUtils.highBit(dq.peekFirst());
            int y = DigitUtils.lowBit(dq.removeFirst());
            for (int[] d : dirs) {
                int nx = d[0] + x;
                int ny = d[1] + y;
                if (nx < 0 || ny < 0 || nx >= n || ny >= m || mat[nx][ny] == '#' ||
                        dists[nx][ny] <= dists[x][y] + 1) {
                    continue;
                }
                dists[nx][ny] = dists[x][y] + 1;
                dq.addLast(DigitUtils.asLong(nx, ny));
            }
        }
    }
}
