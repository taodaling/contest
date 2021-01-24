package contest;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.SequenceUtils;

import java.util.Deque;

public class FlightPlan {
    public long fly(int R, int C, int[] H, int cup, int cdn, int clr) {
        long[][] mat = new long[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                mat[i][j] = H[i * C + j];
            }
        }
        LongDeque dq = new LongDequeImpl(R * C);
        long[][] dists = new long[R][C];
        long inf = (long) 1e18;
        int[][] dirs = new int[][]{
                {-1, 0},
                {0, 1},
                {1, 0},
                {0, -1}
        };
        long ans = inf;
        for (int h : H) {
            if (h < mat[0][0] || h < mat[R - 1][C - 1]) {
                continue;
            }
            long extra = cup * (h - mat[0][0]) + cdn * (h - mat[R - 1][C - 1]);
            dq.clear();
            SequenceUtils.deepFill(dists, inf);
            dists[0][0] = 0;
            dq.addLast(DigitUtils.asLong(0, 0));
            while (!dq.isEmpty()) {
                long headV = dq.removeFirst();
                int x = DigitUtils.highBit(headV);
                int y = DigitUtils.lowBit(headV);
                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int ny = y + d[1];
                    if (nx < 0 || ny < 0 || nx >= R || ny >= C || mat[nx][ny] > h) {
                        continue;
                    }
                    if (dists[nx][ny] > dists[x][y] + 1) {
                        dists[nx][ny] = dists[x][y] + 1;
                        dq.addLast(DigitUtils.asLong(nx, ny));
                    }
                }
            }
            if (dists[R - 1][C - 1] == inf) {
                continue;
            }
            ans = Math.min(ans, extra + dists[R - 1][C - 1] * clr);
        }
        return ans;
    }
}
