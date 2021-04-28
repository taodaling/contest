package on2021_04.on2021_04_21_Codeforces___Codeforces_Round__202__Div__1_.D__Turtles;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Matrix;
import template.math.ModMatrix;
import template.utils.SequenceUtils;

public class DTurtles {
    int mod = (int) 1e9 + 7;
    char[][] mat;
    int[][] dp;
    int n;
    int m;

    public int way(int startX, int startY, int endX, int endY) {
        SequenceUtils.deepFill(dp, 0);
        dp[startX][startY] = 1;
        if (mat[startX][startY] == '#') {
            return 0;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0 || mat[i][j] == '#') {
                    continue;
                }
                if (i > 0) {
                    dp[i][j] += dp[i - 1][j];
                }
                if (j > 0) {
                    dp[i][j] += dp[i][j - 1];
                }
                dp[i][j] = DigitUtils.modWithoutDivision(dp[i][j], mod);
            }
        }
        return dp[endX][endY];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
        }
        dp = new int[n][m];
        ModMatrix matrix = new ModMatrix(2, 2);
        int[][] start = new int[][]{
                {1, 0},
                {0, 1}
        };
        int[][] end = new int[][]{
                {n - 1, m - 2},
                {n - 2, m - 1}
        };

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrix.set(i, j, way(start[i][0], start[i][1], end[j][0], end[j][1]));
            }
        }

        int ans = ModMatrix.determinant(matrix, mod);
        out.println(ans);
    }
}
