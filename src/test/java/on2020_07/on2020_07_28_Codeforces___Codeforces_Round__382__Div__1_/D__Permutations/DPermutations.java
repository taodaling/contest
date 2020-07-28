package on2020_07.on2020_07_28_Codeforces___Codeforces_Round__382__Div__1_.D__Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.XorMatrix;

public class DPermutations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] x = new int[m];
        int[] y = new int[m];

        XorMatrix mat = new XorMatrix(n, n);
        for (int i = 0; i < m; i++) {
            x[i] = in.readInt() - 1;
            y[i] = in.readInt() - 1;
            mat.set(x[i], y[i], true);
        }
        XorMatrix inv = XorMatrix.inverse(mat);
        XorMatrix adjoint = inv;
        XorMatrix cofactor = XorMatrix.transpose(adjoint);
        for(int i = 0; i < m; i++){
            if(cofactor.get(x[i], y[i])){
                out.println("NO");
            }else{
                out.println("YES");
            }
        }
    }
}
