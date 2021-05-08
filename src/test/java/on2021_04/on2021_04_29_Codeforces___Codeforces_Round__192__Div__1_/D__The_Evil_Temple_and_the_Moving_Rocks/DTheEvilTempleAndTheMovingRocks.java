package on2021_04.on2021_04_29_Codeforces___Codeforces_Round__192__Div__1_.D__The_Evil_Temple_and_the_Moving_Rocks;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DTheEvilTempleAndTheMovingRocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int sz = 10;
        int[][][] mat = new int[sz][sz][];

        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                if (i < sz / 2) {
                    if (j < sz / 2) {
                        mat[i][j] = new int[]{0, 1};
                    } else {
                        mat[i][j] = new int[]{1, 0};
                    }
                } else {
                    if (j >= sz / 2) {
                        mat[i][j] = new int[]{0, -1};
                    } else {
                        mat[i][j] = new int[]{-1, 0};
                    }
                }
            }
        }

        mat[sz / 2 - 1][sz / 2] = null;

        int hit = move(mat, sz / 2 - 1, sz / 2 - 1, sz);
        out.println(hit);
    }

    public int move(int[][][] mat, int x, int y, int n) {
        int[][] ts = new int[n][n];
        int time = 1;
        boolean moved = false;
        assert mat[x][y] != null;
        while (true) {
            if (ts[x][y] >= time) {
                break;
            }
            ts[x][y] = time;
            assert mat[x][y] != null;
            int nx = x + mat[x][y][0];
            int ny = y + mat[x][y][1];
            if (nx < 0 || nx >= n || ny < 0 || ny >= n) {
                if (moved) {
                    time++;
                }
                break;
            }
            if (mat[nx][ny] == null) {
                moved = true;
                mat[nx][ny] = mat[x][y];
                mat[x][y] = null;
            } else {
                if (moved) {
                    time++;
                    moved = false;
                }
            }

            x = nx;
            y = ny;
        }

        return time - 1;
    }
}
