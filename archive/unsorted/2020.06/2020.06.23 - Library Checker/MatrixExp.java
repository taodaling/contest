package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.ModSparseMatrix;
import template.math.ModVectorLinearRecurrenceSolver;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;

public class MatrixExp {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Modular mod = new Modular(1e9 + 7);

        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        int[] vec = new int[n];
        for (int i = 0; i < n; i++) {
            vec[i] = in.readInt();
        }

        long k = in.readLong();
        ModVectorLinearRecurrenceSolver solver = new ModVectorLinearRecurrenceSolver(new ModSparseMatrix(new ModMatrix(mat)), vec, mod);

        int[] ans = solver.solve(k);
        for (int x : ans) {
            out.append(x).append(' ');
        }
    }
}
