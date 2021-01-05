package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class WalkingAroundBerhattan {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n * 2 + 10][m * 2 + 10];
        int[][] div = new int[n * 2 + 10][m * 2 + 10];
        SequenceUtils.deepFill(div, 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i * 2 + 1][j * 2 + 1] = in.rc() - '0';
            }
        }
        int x = 0;
        int y = 0;
        int[][] dirs = new int[][]{
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };
        long ans = 0;
        int choice = 3;
        in.skipBlank();
        char[] s = in.readLine().trim().toCharArray();
        for (char c : s) {
            if (c == 'M') {
                x += dirs[choice][0];
                y += dirs[choice][1];
                for (int[] dir : dirs) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    if (nx < 0 || nx >= 2 * n + 1 || ny < 0 || ny >= 2 * m + 1) {
                        continue;
                    }
                    int acquire = mat[nx][ny] / div[nx][ny];
                    div[nx][ny] = 2;
                    ans += acquire;
                }
                x += dirs[choice][0];
                y += dirs[choice][1];
            } else if (c == 'L') {
                choice = (choice + 3) % 4;
            } else if (c == 'R') {
                choice = (choice + 1) % 4;
            }
        }
        out.println(ans);
    }
}
