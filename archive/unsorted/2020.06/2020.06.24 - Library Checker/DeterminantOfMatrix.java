package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.ModSparseMatrix;
import template.math.Modular;
import template.math.Power;

public class DeterminantOfMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        int n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        ModMatrix matrix = new ModMatrix(mat);
        int ans = new ModSparseMatrix(matrix).determinant(pow);
        out.println(ans);
    }
}
