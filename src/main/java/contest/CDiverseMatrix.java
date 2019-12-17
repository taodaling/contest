package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CDiverseMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        if (n == 1 && m == 1) {
            out.println(0);
            return;
        }

        if (n == 1) {
            for(int i = 1; i <= m; i++){
                out.append(i + 1).append(' ');
            }
            out.println();
            return;
        }
        if (m == 1) {
            for(int i = 1; i <= n; i++){
                out.append(i + 1).println();
            }
            return;
        }

        int[][] mat = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            int mul = i == 1 ? 1 : (m + i);
            for (int j = 1; j <= m; j++) {
                mat[i][j] = (j + 1) * mul;
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }
    }
}
