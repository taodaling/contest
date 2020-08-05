package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.LCMs;
import template.utils.Debug;

import java.util.Arrays;
import java.util.HashSet;

public class DModuloMatrix {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = 504;//in.readInt();
        long[][] mat = new long[n][n];
        EulerSieve sieve = new EulerSieve(10000);
        int[] row = new int[n];
        int[] col = new int[n];
        for (int i = 0; i < n / 2; i++) {
            row[i] = sieve.get(i * 2 + 3);
            col[i] = sieve.get(i * 2 + 4);
        }

        row = zigzag(row, 0, n / 2 - 1);
        col = zigzag(col, 0, n / 2 - 1);

        //debug.debug("row", row);
        //debug.debug("col", col);

        int[] expandRow = new int[n];
        int[] expandCol = new int[n];
        for (int i = 0; i < n; i += 2) {
            expandRow[i] = row[i / 2];
            expandRow[i + 1] = 2 * row[i / 2];
            expandCol[i] = col[i / 2];
            expandCol[i + 1] = 3 * col[i / 2];
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i + j) % 2 == 0) {
                    mat[i][j] = expandRow[i] * expandCol[j];
                }
            }
        }

        int[][] dirs = new int[][]{
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if ((i + j) % 2 == 0) {
                    continue;
                }
                long lcm = 1;
                for (int[] dir : dirs) {
                    int x = i + dir[0];
                    int y = j + dir[1];
                    if (x < 0 || y < 0 || x >= n || y >= n) {
                        continue;
                    }
                    lcm = LCMs.lcm(lcm, mat[x][y]);
                }
                if (i % 2 == 0) {
                    lcm *= 2;
                }
                mat[i][j] = lcm + 1;
            }
        }

        long max = 0;
        int m = in.readInt();
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= m; j++) {
                max = Math.max(mat[i][j], max);
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }

        debug.debug("max", max);
    }

    public int[] zigzag(int[] data, int l, int r) {
        int[] ans = new int[data.length];
        int offset = 0;
        while (l <= r) {
            ans[offset++] = data[l];
            if (r >= l + 1) {
                ans[offset++] = data[r];
            }
            l++;
            r--;
        }
        return ans;
    }
}
