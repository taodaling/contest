package on2021_03.on2021_03_16_Luogu.Task;





import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectOnGridProblem;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }
        int[][] maxSquare = RectOnGridProblem.maxSquareContainsAtMostKDistinctNumbers(mat, q);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(maxSquare[i][j]).append(' ');
            }
            out.println();
        }
    }
}
