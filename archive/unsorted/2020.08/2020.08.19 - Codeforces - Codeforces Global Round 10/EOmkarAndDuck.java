package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EOmkarAndDuck {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        n = in.readInt();
        long[][] mat = new long[n][n];
        for (int i = 0; i < n; i++) {
            if (i % 2 == 1) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                mat[i][j] = 1L << (i + j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j]).append(' ');
            }
        }
        out.println().flush();

        int q = in.readInt();
        while (q-- > 0) {
            long dist = in.readLong();
            trace(dist, mat, 0, 0);
            out.flush();
        }
    }

    FastOutput out;
    int n;

    public void output(int x, int y) {
        out.append(x + 1).append(' ').append(y + 1).println();
    }

    public void trace(long dist, long[][] mat, int x, int y) {
        output(x, y);
        dist -= mat[x][y];
        if (x == n - 1 && y == n - 1) {
            return;
        }
        if (x % 2 == 0) {
            if (y + 1 < n && Long.lowestOneBit(dist) == mat[x][y + 1]) {
                trace(dist, mat, x, y + 1);
            } else {
                trace(dist, mat, x + 1, y);
            }
        } else {
            if (x + 1 < n && Long.lowestOneBit(dist) == mat[x + 1][y]) {
                trace(dist, mat, x + 1, y);
            } else {
                trace(dist, mat, x, y + 1);
            }
        }
    }
}
