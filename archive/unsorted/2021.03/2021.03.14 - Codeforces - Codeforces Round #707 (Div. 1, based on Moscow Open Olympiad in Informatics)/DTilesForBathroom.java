package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectOnGridProblem;

public class DTilesForBathroom {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[][] mat = new int[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                mat[i][j] = in.ri();
            }
        }
        int[][] maxSquare = RectOnGridProblem.maxSquareContainsAtMostKDistinctNumbers(mat, q);
        int[] ans = new int[n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans[maxSquare[i][j]]++;
            }
        }
        for (int i = ans.length - 2; i >= 1; i--) {
            ans[i] += ans[i + 1];
        }
        for(int i = 1; i <= n; i++){
            out.println(ans[i]);
        }

    }
}
