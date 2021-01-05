package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.LatinSquare;
import template.utils.Debug;

public class ELatinSquare {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri() - 1;
            }
        }
        LatinSquare ls = new LatinSquare(mat, false);
        for (int i = 0; i < q; i++) {
            debug.debug("ls", ls);
            char c = in.rc();
            if (c == 'U') {
                ls.up(1);
            } else if (c == 'D') {
                ls.down(1);
            } else if (c == 'L') {
                ls.left(1);
            } else if (c == 'R') {
                ls.right(1);
            } else if (c == 'I') {
                ls.rowInv();
            } else if (c == 'C') {
                ls.colInv();
            }
        }
        debug.debug("ls", ls);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                out.append(ls.get(i, j) + 1).append(' ');
            }
            out.println();
        }
        out.println();
    }
}
