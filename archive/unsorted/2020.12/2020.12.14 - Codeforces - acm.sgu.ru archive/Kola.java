package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Kola {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i], 0);
        }
        int x = -1;
        int y = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 'P') {
                    x = i;
                    y = j;
                }
            }
        }

        while (x < n) {
            if (mat[x][y] == '/') {
                y--;
                if (y < 0 || mat[x][y] == '\\') {
                    out.append(-1);
                    return;
                }
            } else if (mat[x][y] == '\\') {
                y++;
                if (y == m || mat[x][y] == '/') {
                    out.append(-1);
                    return;
                }
            }
            x++;
        }
        out.append(y + 1);
    }
}
