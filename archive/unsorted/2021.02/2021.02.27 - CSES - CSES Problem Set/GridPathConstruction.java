package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.GridHamiltonPath;
import template.problem.GridHamiltonPathBeta;
import template.utils.Debug;

public class GridPathConstruction {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("testNumber", testNumber);
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[2];
        int[] b = new int[2];
        for (int i = 0; i < 2; i++) {
            a[i] = in.ri() - 1;
        }
        for (int i = 0; i < 2; i++) {
            b[i] = in.ri() - 1;
        }
//        debug.debug("input", "" + n + " " + m + " " + (a[0] + 1) + " " + (a[1] + 1) + " "
//                + (b[0] + 1) + " " + (b[1] + 1));
        GridHamiltonPathBeta beta = new GridHamiltonPathBeta();
        char[][] ans = beta.solve(n, m, a[0], a[1], b[0], b[1]);
        if (ans == null) {
            out.println("NO");
            return;
        }
        out.println("YES");
        int x = a[0];
        int y = a[1];
        while (!(x == b[0] && y == b[1])) {
            switch (ans[x][y]) {
                case 'L':
                    out.append('L');
                    y--;
                    break;
                case 'R':
                    out.append('R');
                    y++;
                    break;
                case 'U':
                    out.append('D');
                    x++;
                    break;
                case 'D':
                    out.append('U');
                    x--;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        out.println();
    }
}
