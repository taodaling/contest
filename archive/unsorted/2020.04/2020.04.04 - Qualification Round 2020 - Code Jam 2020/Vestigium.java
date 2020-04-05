package contest;


import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Vestigium {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        int trace = 0;
        for (int i = 0; i < n; i++) {
            trace += mat[i][i];
        }

        int row = 0;
        int col = 0;
        boolean[] appear = new boolean[n + 1];
        for (int i = 0; i < n; i++) {
            Arrays.fill(appear, false);
            for (int j = 0; j < n; j++) {
                if (appear[mat[i][j]]) {
                    row++;
                    break;
                }
                appear[mat[i][j]] = true;
            }
            Arrays.fill(appear, false);
            for (int j = 0; j < n; j++) {
                if (appear[mat[j][i]]) {
                    col++;
                    break;
                }
                appear[mat[j][i]] = true;
            }
        }

        out.printf("Case #%d: %d %d %d", testNumber, trace, row, col).println();
    }
}
