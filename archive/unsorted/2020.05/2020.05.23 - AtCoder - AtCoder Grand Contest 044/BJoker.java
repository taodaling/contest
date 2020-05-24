package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class BJoker {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = 1;
            }
        }

        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = Math.min(i, j);
                dist[i][j] = Math.min(dist[i][j], n - 1 - i);
                dist[i][j] = Math.min(dist[i][j], n - 1 - j);
            }
        }

        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        long total = 0;
        IntegerDeque dq = new IntegerDequeImpl(n * n);
        for (int i = 0; i < n * n; i++) {
            int perm = in.readInt() - 1;
            int r = row(perm);
            int c = col(perm);
            mat[r][c] = 0;
            total += dist[r][c];
            dq.addLast(perm);

            while (!dq.isEmpty()) {
                int pos = dq.removeFirst();
                int x = row(pos);
                int y = col(pos);

                for (int[] dir : dirs) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx < 0 || ny < 0 || nx >= n || ny >= n) {
                        continue;
                    }
                    if (dist[nx][ny] <= dist[x][y] + mat[x][y]) {
                        continue;
                    }
                    dist[nx][ny] = dist[x][y] + mat[x][y];
                    if (mat[nx][ny] == 0) {
                        dq.addFirst(id(nx, ny));
                    } else {
                        dq.addLast(id(nx, ny));
                    }
                }
            }
        }

        out.println(total);
    }

    int n;

    public int row(int i) {
        return i / n;
    }

    public int col(int i) {
        return i % n;
    }

    public int id(int i, int j) {
        return i * n + j;
    }

}
