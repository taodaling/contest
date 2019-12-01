package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        mat = new int[n][m];
        buf = new int[n][m];
        time = new int[n][m];
        border = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() == 'X' ? 1 : 0;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                if (i == 0 || i == n - 1 || j == 0 || j == m - 1) {
                    border[i][j] = true;
                    continue;
                }
                int cnt = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int mi = i + dx;
                        int mj = j + dy;
                        cnt += mat[mi][mj];
                    }
                }

                if (cnt != 9) {
                    border[i][j] = true;
                }
            }
        }

        int l = 0;
        int r = Math.min(n / 2, m / 2);
        while (l < r) {
            int mid = (l + r + 1) >>> 1;
            if (check(mid)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        check(l);
        out.println(l);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(time[i][j] == 0 ? 'X' : '.');
            }
            out.println();
        }
    }

    LongDeque deque = new LongDeque(1000000);
    int[][] buf;
    int[][] time;
    boolean[][] border;
    int[][] mat;

    public boolean check(int t) {
        int n = mat.length;
        int m = mat[0].length;
        SequenceUtils.deepFill(time, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!border[i][j]) {
                    continue;
                }
                deque.addLast(DigitUtils.asLong(i, j));
                time[i][j] = 0;
            }
        }
        while (!deque.isEmpty()) {
            long head = deque.removeFirst();
            int x = DigitUtils.highBit(head);
            int y = DigitUtils.lowBit(head);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int mx = x + dx;
                    int my = y + dy;
                    if (mx < 0 || mx >= n || my < 0 || my >= m || mat[mx][my] == 0 ||
                            time[mx][my] != -1) {
                        continue;
                    }
                    time[mx][my] = time[x][y] + 1;
                    deque.addLast(DigitUtils.asLong(mx, my));
                }
            }
        }

        SequenceUtils.deepFill(buf, 0);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (time[i][j] >= t) {
                    deque.addLast(DigitUtils.asLong(i, j));
                    time[i][j] = 0;
                } else {
                    time[i][j] = -1;
                }
            }
        }

        while (!deque.isEmpty()) {
            long head = deque.removeFirst();
            int x = DigitUtils.highBit(head);
            int y = DigitUtils.lowBit(head);
            buf[x][y] = 1;
            if (time[x][y] >= t) {
                continue;
            }
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int mx = x + dx;
                    int my = y + dy;
                    if (time[mx][my] != -1) {
                        continue;
                    }
                    if (mx < 0 || mx >= n || my < 0 || my >= m || mat[mx][my] == 0) {
                        return false;
                    }
                    time[mx][my] = time[x][y] + 1;
                    deque.addLast(DigitUtils.asLong(mx, my));
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (buf[i][j] != mat[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
