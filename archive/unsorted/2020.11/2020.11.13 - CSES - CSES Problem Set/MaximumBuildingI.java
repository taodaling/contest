package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MaximumRectangleAreaProblem;
import template.utils.Debug;

public class MaximumBuildingI {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() == '*' ? 1 : 0;
            }
        }
        int[] ans = MaximumRectangleAreaProblem.solve((i, j) -> mat[i][j], n, m);
        debug.debugMatrix("mat", mat);
        debug.debug("ans", ans);
        out.println(ans[0]);
    }
}
