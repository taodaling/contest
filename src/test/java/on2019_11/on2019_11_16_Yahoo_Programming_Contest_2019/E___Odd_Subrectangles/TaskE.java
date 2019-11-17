package on2019_11.on2019_11_16_Yahoo_Programming_Contest_2019.E___Odd_Subrectangles;



import template.FastInput;
import template.FastOutput;
import template.GeneralizedLinearBasis;
import template.NumberTheory;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        boolean[][] mat = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() == '1';
            }
        }
        GeneralizedLinearBasis glb = new GeneralizedLinearBasis(m);
        for (int i = 0; i < n; i++) {
            glb.add(mat[i]);
        }

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        int rowSet = mod.subtract(pow.pow(2, n), pow.pow(2, n - glb.size()));
        int ans = mod.mul(rowSet, pow.pow(2, m - 1));

        out.println(ans);
    }
}
