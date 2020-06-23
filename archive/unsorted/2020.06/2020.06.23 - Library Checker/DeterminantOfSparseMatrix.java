package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModSparseMatrix;
import template.math.Modular;

public class DeterminantOfSparseMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Modular mod = new Modular(998244353);
        ModSparseMatrix mat = new ModSparseMatrix(n, k);
        for(int i = 0; i < k; i++){
            int a = in.readInt();
            int b = in.readInt();
            int v = in.readInt();
            mat.set(i, a, b, v);
        }

        int ans = mat.determinant(mod);
        out.println(ans);
    }
}
