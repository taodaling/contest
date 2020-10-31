package contest;

import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class TwoRobots {
    public int move(String[] plan) {
        int n = plan.length;
        int m = plan[0].length();
        int[][][][] dp = new int[n][m][n][m];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, inf);
        int[] a = new int[2];
        int[] A = new int[2];
        int[] b = new int[2];
        int[] B = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                char c = plan[i].charAt(j);
                if (c == 'a') {
                    a[0] = i;
                    a[1] = j;
                }
                if (c == 'A') {
                    A[0] = i;
                    A[1] = j;
                }
                if (c == 'b') {
                    b[0] = i;
                    b[1] = j;
                }
                if (c == 'B') {
                    B[0] = i;
                    B[1] = j;
                }
            }
        }
        dp[A[0]][A[1]][B[0]][B[1]] = 0;
        Deque<int[]> dq = new ArrayDeque<>();
        dq.addLast(new int[]{A[0], A[1], B[0], B[1]});
        int[][] dirs = new int[][]{
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            int x0 = head[0];
            int y0 = head[1];
            int x1 = head[2];
            int y1 = head[3];
            int dist = dp[x0][y0][x1][y1];

            for (int[] d1 : dirs) {
                int nx0 = x0 + d1[0];
                int ny0 = y0 + d1[1];
                if(nx0 < 0 || nx0 >= n || ny0 < 0 || ny0 >= m || plan[nx0].charAt(ny0) == '#'){
                    continue;
                }
                for (int[] d2 : dirs) {
                    int nx1 = x1 + d2[0];
                    int ny1 = y1 + d2[1];
                    if(nx1 < 0 || nx1 >= n || ny1 < 0 || ny1 >= m || plan[nx1].charAt(ny1) == '#'){
                        continue;
                    }

                    if (nx0 == nx1 && ny0 == ny1) {
                        continue;
                    }
                    if (nx0 == x1 && ny0 == y1 && nx1 == x0 && ny1 == y0) {
                        continue;
                    }
                    if (dp[nx0][ny0][nx1][ny1] <= dist + 1) {
                        continue;
                    }
                    dp[nx0][ny0][nx1][ny1] = dist + 1;
                    dq.addLast(new int[]{nx0, ny0, nx1, ny1});
                }
            }
        }

        int ans = dp[a[0]][a[1]][b[0]][b[1]];
        return ans == inf ? -1 : ans;
    }
}
