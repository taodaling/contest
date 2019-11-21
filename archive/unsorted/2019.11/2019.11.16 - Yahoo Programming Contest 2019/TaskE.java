package contest;

import template.FastInput;
import template.FastOutput;
import template.GeneralizedLinearBasis;

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

        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        int rowSet = mod.subtract(pow.pow(2, n), pow.pow(2, n - glb.size()));
        int ans = mod.mul(rowSet, pow.pow(2, m - 1));

        out.println(ans);
    }
}
