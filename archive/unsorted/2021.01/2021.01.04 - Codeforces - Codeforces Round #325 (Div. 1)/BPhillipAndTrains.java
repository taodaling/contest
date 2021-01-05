package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class BPhillipAndTrains {
    public boolean isBlock(char c) {
        return c >= 'A' && c <= 'Z';
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        char[][] mat = new char[3][n + 3];
        SequenceUtils.deepFill(mat, '.');
        for (int i = 0; i < 3; i++) {
            in.rs(mat[i], 0);
        }
        Deque<int[]> dq = new ArrayDeque<>(3 * (n + 3));
        boolean[][] access = new boolean[3][n + 3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 's') {
                    access[i][j] = true;
                    dq.add(new int[]{i, j});
                }
            }
        }

        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            debug.debugArray("head", head);
            int x = head[0];
            int y = head[1];
            if (y >= n) {
                out.println("YES");
                return;
            }
            if (isBlock(mat[x][y + 1])) {
                continue;
            }
            for (int v = -1; v <= 1; v++) {
                int nx = x + v;
                if (nx < 0 || nx >= 3) {
                    continue;
                }
                boolean valid = true;
                for (int i = 1; i <= 3; i++) {
                    if (isBlock(mat[nx][i + y])) {
                        valid = false;
                    }
                }
                int ny = y + 3;
                if (valid && !access[nx][ny]) {
                    access[nx][ny] = true;
                    dq.add(new int[]{nx, ny});
                }
            }
        }

        out.println("NO");
    }
}
