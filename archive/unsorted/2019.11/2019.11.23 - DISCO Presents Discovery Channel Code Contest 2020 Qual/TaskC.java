package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int k = in.readInt();
        char[][] mat = new char[h][w];
        for (int i = 0; i < h; i++) {
            in.readString(mat[i], 0);
        }

        int[][] assign = new int[h][w];
        int num = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (mat[i][j] != '#') {
                    continue;
                }
                num++;
                //horizontal first
                int l = j;
                int r = j;
                while (l - 1 >= 0 && assign[i][l - 1] == 0 &&
                        mat[i][l - 1] == '.') {
                    l--;
                }
                while (r + 1 <= w - 1 && assign[i][r + 1] == 0 &&
                        mat[i][r + 1] == '.') {
                    r++;
                }
                int u = i;
                int d = i;
                while (u - 1 >= 0) {
                    boolean valid = true;
                    for (int t = l; t <= r; t++) {
                        valid = valid && assign[u - 1][t] == 0 &&
                                mat[u - 1][t] == '.';
                    }
                    if (!valid) {
                        break;
                    }
                    u--;
                }
                while (d + 1 <= h - 1) {
                    boolean valid = true;
                    for (int t = l; t <= r; t++) {
                        valid = valid && assign[d + 1][t] == 0 &&
                                mat[d + 1][t] == '.';
                    }
                    if (!valid) {
                        break;
                    }
                    d++;
                }
                for (int a = u; a <= d; a++) {
                    for (int b = l; b <= r; b++) {
                        assign[a][b] = num;
                    }
                }
            }
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                out.append(assign[i][j]).append(' ');
            }
            out.println();
        }
    }
}
