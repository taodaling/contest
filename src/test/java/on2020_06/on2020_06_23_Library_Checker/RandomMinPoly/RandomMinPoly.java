package on2020_06.on2020_06_23_Library_Checker.RandomMinPoly;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModSparseMatrix;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;

public class RandomMinPoly {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        ModSparseMatrix mat = new ModSparseMatrix(n, m);
        for (int i = 0; i < m; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            mat.set(i, x, y, z);
        }

        Modular mod = new Modular(1e9 + 7);
        IntegerList p = mat.getMinimalPolynomialByRandom(mod);
        for (int i = 0; i < p.size(); i++) {
            out.append(p.get(i)).append(' ');
        }
    }
}
