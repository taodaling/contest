package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.GridUtils;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

public class Quadrocopter {

    int h, w;

    boolean valid(int x, int y) {
        return x >= 0 && x < h && y >= 0 && y < w;
    }

    final int NOT_IN_CIRCLE = 1;
    final int IN_CIRCLE = 2;
    int[][] type;
    int[][] firstMeet;
    int[][][] to;
    Deque<int[]> dq;

    int dfs(int x, int y, int time) {
        if (!valid(x, y)) {
            return -1;
        }
        if (type[x][y] != 0) {
            int len = 0;
            while (true) {
                len++;
                int[] back = dq.removeLast();
                type[back[0]][back[1]] = IN_CIRCLE;
                if (back[0] == x && back[1] == y) {
                    break;
                }
            }
            return len;
        }
        type[x][y] = NOT_IN_CIRCLE;
        firstMeet[x][y] = time;
        dq.addLast(new int[]{x, y});
        return dfs(to[x][y][0] + x, to[x][y][1] + y, time + 1);
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        h = in.ri();
        w = in.ri();
        dq = new ArrayDeque<>(h * w);
        type = new int[h][w];
        firstMeet = new int[h][w];
        int[] vova = in.ri(2);
        int[] fly = in.ri(2);
        for (int i = 0; i < 2; i++) {
            vova[i]--;
            fly[i]--;
        }

        int[][] mat = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                mat[i][j] = in.ri();
            }
        }
        to = new int[h][w][];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char c = in.rc();
                if (c == 'R') {
                    to[i][j] = new int[]{0, 1};
                } else if (c == 'L') {
                    to[i][j] = new int[]{0, -1};
                } else if (c == 'U') {
                    to[i][j] = new int[]{-1, 0};
                } else {
                    to[i][j] = new int[]{1, 0};
                }
            }
        }

        int inf = (int) 1e9;
        int[][] dist = new int[h][w];
        SequenceUtils.deepFill(dist, inf);
        PriorityQueue<int[]> pq = new PriorityQueue<>(h * w, Comparator.comparingInt(x -> x[2]));
        pq.add(new int[]{vova[0], vova[1], 0});
        while (!pq.isEmpty()) {
            int[] head = pq.remove();
            int x = head[0];
            int y = head[1];
            int d = head[2];
            if (d >= dist[x][y]) {
                continue;
            }
            dist[x][y] = d;
            for (int[] dir : GridUtils.DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (!valid(nx, ny)) {
                    continue;
                }
                pq.add(new int[]{nx, ny, d + mat[nx][ny]});
            }
        }

        int ans = inf;
        int circleSize = dfs(fly[0], fly[1], 0);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (type[i][j] == NOT_IN_CIRCLE && firstMeet[i][j] >= dist[i][j]) {
                    ans = Math.min(ans, firstMeet[i][j]);
                } else if (type[i][j] == IN_CIRCLE) {
                    int c = DigitUtils.ceilDiv(dist[i][j] - firstMeet[i][j], circleSize);
                    c = Math.max(0, c);
                    ans = Math.min(ans, c * circleSize + firstMeet[i][j]);
                }
            }
        }
        out.println(ans == inf ? -1 : ans);

        debug.debugMatrix("dist", dist);
        debug.debugMatrix("firstMeet", firstMeet);
        debug.debugMatrix("type", type);
    }


}