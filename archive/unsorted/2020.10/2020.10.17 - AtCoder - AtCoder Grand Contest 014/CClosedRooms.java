package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class CClosedRooms {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int h = in.readInt();
        int w = in.readInt();
        int k = in.readInt();
        char[][] grid = new char[h][w];
        int[] start = null;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                grid[i][j] = in.readChar();
                if (grid[i][j] == 'S') {
                    start = new int[]{i, j};
                }
            }
        }
        assert start != null;
        Deque<int[]> dq = new ArrayDeque<>();
        dq.addLast(start);
        int[][] dist = new int[h][w];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dist, inf);
        dist[start[0]][start[1]] = 0;
        int[][] dirs = new int[][]{
                {0, 1},
                {0, -1},
                {1, 0},
                {-1, 0}
        };
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            for (int[] dir : dirs) {
                int x = head[0] + dir[0];
                int y = head[1] + dir[1];
                if (x < 0 || x >= h || y < 0 || y >= w) {
                    continue;
                }
                if (grid[x][y] == '#') {
                    continue;
                }
                if (dist[head[0]][head[1]] + 1 < dist[x][y]) {
                    dist[x][y] = dist[head[0]][head[1]] + 1;
                    dq.addLast(new int[]{x, y});
                }
            }
        }

        int ans = inf;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (dist[i][j] <= k) {
                    ans = Math.min(ans, DigitUtils.ceilDiv(i, k));
                    ans = Math.min(ans, DigitUtils.ceilDiv(j, k));
                    ans = Math.min(ans, DigitUtils.ceilDiv(h - 1 - i, k));
                    ans = Math.min(ans, DigitUtils.ceilDiv(w - 1 - j, k));
                }
            }
        }

        out.print(ans + 1);

    }
}
