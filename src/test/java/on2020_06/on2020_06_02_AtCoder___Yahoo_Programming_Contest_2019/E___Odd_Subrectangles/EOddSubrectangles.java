package on2020_06.on2020_06_02_AtCoder___Yahoo_Programming_Contest_2019.E___Odd_Subrectangles;



import template.datastructure.BitSet;
import template.datastructure.GenericLinearBasis;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

public class EOddSubrectangles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        BitSet[] mat = new BitSet[n];
        for (int i = 0; i < n; i++) {
            mat[i] = new BitSet(m);
            for (int j = 0; j < m; j++) {
                if (in.readChar() == '1') {
                    mat[i].set(j);
                } else {
                    mat[i].clear(j);
                }
            }
        }
        GenericLinearBasis glb = new GenericLinearBasis(m);
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
