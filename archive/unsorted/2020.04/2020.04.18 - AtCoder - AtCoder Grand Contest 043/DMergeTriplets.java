package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DMergeTriplets {
    Modular mod;
    Combination comb;
    int zero;
    int[][] f;
    int inv2;

    public int f(int i, int j) {
        if (i == 0) {
            return j == zero ? 1 : 0;
        }
        if (i < 0 || j >= f[i].length || j < 0) {
            return 0;
        }
        if (f[i][j] == -1) {
            f[i][j] = f(i - 1, j - 1);
            f[i][j] = mod.plus(f[i][j], mod.mul(i - 1, f(i - 2, j + 1)));
            f[i][j] = mod.plus(f[i][j], mod.mul(pick2(i - 1), f(i - 3, j)));
        }
        return f[i][j];
    }

    public int pick2(int n) {
        return mod.mul(n, (n - 1));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt() * 3;
        mod = new Modular(in.readInt());
        comb = new Combination(n, mod);
        zero = n;
        f = new int[n + 1][n + n + 1];
        SequenceUtils.deepFill(f, -1);

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            ans = mod.plus(ans, f(n, i + zero));
        }

        out.println(ans);
    }
}
