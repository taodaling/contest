package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class RobotAnnihilator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[][] building = new boolean[n][m];
        int[][] dirs = new int[][]{
                {1, 0, 'D'},
                {0, -1, 'L'},
                {-1, 0, 'U'},
                {0, 1, 'R'}
        };
        int x = in.ri() - 1;
        int y = in.ri() - 1;
        while (true) {
            building[x][y] = true;
            boolean find = false;
            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                    continue;
                }
                if (building[nx][ny]) {
                    continue;
                }
                out.append((char) d[2]);
                x = nx;
                y = ny;
                find = true;
                break;
            }
            if(!find){
                break;
            }
        }
    }
}
