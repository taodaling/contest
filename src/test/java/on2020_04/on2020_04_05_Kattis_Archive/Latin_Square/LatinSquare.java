package on2020_04.on2020_04_05_Kattis_Archive.Latin_Square;



import template.graph.HungrayAlgoMatrixStyle;
import template.io.FastInput;
import template.io.FastOutput;

public class LatinSquare {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[][] mat = new int[n][n];
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt() - 1;
                if (mat[i][j] >= 0) {
                    used[mat[i][j]] = true;
                }
            }
        }

        out.println("YES");
        HungrayAlgoMatrixStyle ha = new HungrayAlgoMatrixStyle(n, n);
        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }
            ha.reset();
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < n; t++) {
                    if (mat[j][t] == -1) {
                        ha.addEdge(j, t, true);
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                if(!ha.matchLeft(j)){
                    throw new RuntimeException();
                }
            }
            for (int j = 0; j < n; j++) {
                mat[j][ha.leftPartner(j)] = i;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j] + 1).append(' ');
            }
            out.println();
        }
    }
}
