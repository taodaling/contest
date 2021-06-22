package on2021_06.on2021_06_21_Library_Checker.Inverse_Matrix;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Power;

public class InverseMatrix {
    int mod = 998244353;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        ModMatrix mat = new ModMatrix(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                mat.set(i, j, in.ri());
            }
        }
        ModMatrix inv = ModMatrix.inverse(mat, new Power(mod));
        if(inv == null){
            out.println(-1);
            return;
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                out.append(inv.get(i, j)).append(' ');
            }
            out.println();
        }
    }
}
