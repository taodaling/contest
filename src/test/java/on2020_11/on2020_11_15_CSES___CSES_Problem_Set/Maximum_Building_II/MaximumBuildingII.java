package on2020_11.on2020_11_15_CSES___CSES_Problem_Set.Maximum_Building_II;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectOnGridProblem;

public class MaximumBuildingII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }
        long[][] ans = RectOnGridProblem.countAvailableRect((i, j) -> mat[i][j] == '*' ? 1 : 0, n, m);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                out.append(ans[i][j]).append(' ');
            }
            out.println();
        }
    }
}
