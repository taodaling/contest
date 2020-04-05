package on2020_04.on2020_04_05_Kattis_Archive.Superdoku;



import template.graph.HungrayAlgoMatrixStyle;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class Superdoku {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt() - 1;
            }
        }

        boolean valid = true;
        boolean[] occur = new boolean[n];
        for (int i = 0; i < k; i++) {
            Arrays.fill(occur, false);
            for (int j = 0; j < n; j++) {
                if (occur[mat[i][j]]) {
                    valid = false;
                }
                occur[mat[i][j]] = true;
            }
        }


        boolean[][] used = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                if (used[i][mat[j][i]]) {
                    valid = false;
                }
                used[i][mat[j][i]] = true;
            }
        }

        if (!valid) {
            out.println("no");
            return;
        }

        HungrayAlgoMatrixStyle ha = new HungrayAlgoMatrixStyle(n, n);
        for (int i = k; i < n; i++) {
            ha.reset();
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < n; t++) {
                    if (used[j][t]) {
                        continue;
                    }
                    ha.addEdge(j, t, true);
                }
            }
            for (int j = 0; j < n; j++) {
                if (!ha.matchLeft(j)) {
                    debug.debug("i", i);
                    debug.debug("j", j);
                    debug.debug("ha", ha);
                    throw new RuntimeException();
                }
            }
            for (int j = 0; j < n; j++) {
                mat[i][j] = ha.leftPartner(j);
                used[j][mat[i][j]] = true;
            }
        }

        out.println("yes");
        for (int[] row : mat) {
            for (int cell : row) {
                out.append(cell + 1).append(' ');
            }
            out.println();
        }
    }
}
