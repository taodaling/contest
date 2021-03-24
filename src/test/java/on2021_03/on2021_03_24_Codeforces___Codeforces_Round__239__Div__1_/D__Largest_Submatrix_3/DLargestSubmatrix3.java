package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__239__Div__1_.D__Largest_Submatrix_3;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectOnGridProblem;

public class DLargestSubmatrix3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }
        int ans = RectOnGridProblem.maxAreaDistinctRect(mat).a;
        out.println(ans);
    }
}
