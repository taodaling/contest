package on2020_10.on2020_10_26_CSES___CSES_Problem_Set.Counting_Rooms;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.SequenceUtils;

public class CountingRooms {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        mat = new char[n][m];
        int[] a = new int[2];
        int[] b = new int[2];
        prev = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar();
                if (mat[i][j] == 'A') {
                    a = new int[]{i, j};
                }
                if (mat[i][j] == 'B') {
                    b = new int[]{i, j};
                }
            }
        }

        dists = new int[n][m];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dists, inf);
        dists[a[0]][a[1]] = 0;
        LongDeque dq = new LongDequeImpl(n * m);
        dq.addLast(DigitUtils.asLong(a[0], a[1]));
        while (!dq.isEmpty()) {
            int x = DigitUtils.highBit(dq.peekFirst());
            int y = DigitUtils.lowBit(dq.removeFirst());
            for (int i = 0; i < 4; i++) {
                int nx = x + dirs[i][0];
                int ny = y + dirs[i][1];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m || mat[nx][ny] == '#' ||
                        dists[nx][ny] < dists[x][y] + 1) {
                    continue;
                }
                dists[nx][ny] = dists[x][y] + 1;
                prev[nx][ny] = i;
                dq.addLast(DigitUtils.asLong(nx, ny));
            }
        }

        if (dists[b[0]][b[1]] == inf) {
            out.println("NO");
            return;
        }
        out.println("YES");
        int x = b[0];
        int y = b[1];
        StringBuilder ans = new StringBuilder(n * m);
        while (x != a[0] || y != a[1]) {
            ans.append(way[prev[x][y]]);
            int[] d = dirs[prev[x][y]];
            x -= d[0];
            y -= d[1];
        }
        ans.reverse();
        out.println(ans.length());
        out.println(ans);
    }

    char[][] mat;
    int[][] prev;
    int[][] dirs = new int[][]{
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1}
    };
    char[] way = "DRUL".toCharArray();
    int n;
    int m;
    int[][] dists;

}
