package on2020_09.on2020_09_25_Library_Checker.Determinant_of_Sparse_Matrix;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModSparseMatrix;
import template.math.Power;
import template.math.SparseMatrix;

public class DeterminantOfSparseMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int mod = 998_244_353;
        ModSparseMatrix sm = new ModSparseMatrix(n, k);
        for(int i = 0; i < k; i++){
            sm.set(i, in.readInt(), in.readInt(), in.readInt());
        }
        int det = sm.determinant(new Power(mod));
        out.println(det);
    }
}
