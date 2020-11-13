package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.SequenceUtils;

public class FPondSkater {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int k = in.readInt();
        int[] start = new int[2];
        int[] end = new int[2];
        for (int i = 0; i < 2; i++) {
            start[i] = in.readInt() - 1;
        }
        for (int i = 0; i < 2; i++) {
            end[i] = in.readInt() - 1;
        }
        char[][] mat = new char[h][w];
        for (int i = 0; i < h; i++) {
            in.readString(mat[i], 0);
        }
        int inf = (int) 1e9;
        boolean[][] type = new boolean[h][w];
        int[][] dist = new int[h][w];
        SequenceUtils.deepFill(dist, inf);
        dist[start[0]][start[1]] = 0;
        LongDeque dq = new LongDequeImpl(h * w);
        dq.addLast(DigitUtils.asLong(start[0], start[1]));
        while (!dq.isEmpty()) {
            long head = dq.removeFirst();
            int i = DigitUtils.highBit(head);
            int j = DigitUtils.lowBit(head);
            //right
            for (int r = j + 1; r < w && r - j <= k; r++) {
                int x = i;
                int y = r;
                if (dist[x][y] <= dist[i][j] ||
                        dist[x][y] == dist[i][j] + 1 &&
                                type[x][y] || mat[x][y] == '@') {
                    break;
                }
                if (dist[x][y] == dist[i][j] + 1) {
                    continue;
                }
                dist[x][y] = dist[i][j] + 1;
                type[x][y] = true;
                dq.addLast(DigitUtils.asLong(x, y));
            }
            //left
            for (int r = j - 1; r >= 0 && j - r <= k; r--) {
                int x = i;
                int y = r;
                if (dist[x][y] <= dist[i][j] ||
                        dist[x][y] == dist[i][j] + 1 &&
                                type[x][y] || mat[x][y] == '@') {
                    break;
                }
                if (dist[x][y] == dist[i][j] + 1) {
                    continue;
                }
                dist[x][y] = dist[i][j] + 1;
                type[x][y] = true;
                dq.addLast(DigitUtils.asLong(x, y));
            }
            //up
            for (int u = i - 1; u >= 0 && i - u <= k; u--) {
                int x = u;
                int y = j;
                if (dist[x][y] <= dist[i][j] ||
                        dist[x][y] == dist[i][j] + 1 &&
                                !type[x][y] || mat[x][y] == '@') {
                    break;
                }
                if (dist[x][y] == dist[i][j] + 1) {
                    continue;
                }
                dist[x][y] = dist[i][j] + 1;
                type[x][y] = false;
                dq.addLast(DigitUtils.asLong(x, y));
            }
            //down
            for (int u = i + 1; u < h && u - i <= k; u++) {
                int x = u;
                int y = j;
                if (dist[x][y] <= dist[i][j] ||
                        dist[x][y] == dist[i][j] + 1 &&
                                !type[x][y] || mat[x][y] == '@') {
                    break;
                }
                if (dist[x][y] == dist[i][j] + 1) {
                    continue;
                }
                dist[x][y] = dist[i][j] + 1;
                type[x][y] = false;
                dq.addLast(DigitUtils.asLong(x, y));
            }
        }

        int ans = dist[end[0]][end[1]];
        if (ans == inf) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }
}
