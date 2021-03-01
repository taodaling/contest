package contest;

import template.algo.DancingLink;
import template.io.FastInput;
import template.io.FastOutput;

public class P4929DLX {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[][] mat = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri() == 1;
            }
        }
        DancingLink dl = DancingLink.newDenseInstance(mat, n, m);
        int[] sol = dl.getSolution();
        if (sol == null) {
            out.println("No Solution!");
            return;
        }
        for (int x : sol) {
            out.append(x + 1).append(' ');
        }
    }
}
