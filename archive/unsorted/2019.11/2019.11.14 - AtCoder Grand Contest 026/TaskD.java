package contest;


import template.DiscreteMap;
import template.FastInput;
import template.FastOutput;
import template.IntList;
import template.NumberTheory;
import template.SequenceUtils;

public class TaskD {
    int[] heights;
    int[][] f;
    int[] g;
    int n;
    DiscreteMap dm;
    int m;
    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
    NumberTheory.Power pow = new NumberTheory.Power(mod);

    public int f(int i, int j) {
        if (f[i][j] == -1) {
            f[i][j] = 1;
            int r = dm.rankOf(heights[i]);
            if (j >= r) {
                return f[i][j] = 0;
            }
            int lastR = dm.rankOf(heights[i - 1]);

            if (lastR >= r) {
                // inherit
                f[i][j] = mod.subtract(f(i - 1, j), f(i - 1, r));
                return f[i][j];
            }

            if (j <= lastR) {
                // inherit
                f[i][j] = mod.mul(f(i - 1, j), pow.pow(2, heights[i] - heights[i - 1]));

                int plus = mod.mul(g(i - 1), 2);
                plus = mod.mul(plus, mod.subtract(pow.pow(2, heights[i] - heights[i - 1]), 1));
                f[i][j] = mod.plus(plus, f[i][j]);
            } else {
                // self built
                f[i][j] = mod.mul(2, g(i - 1));
                f[i][j] = mod.mul(f[i][j], mod.subtract(pow.pow(2, heights[i] - dm.iThElement(j)), 1));
            }
        }

        return f[i][j];
    }

    public int g(int i) {
        if (g[i] == -1) {
            int r = dm.rankOf(heights[i]);
            int lastR = dm.rankOf(heights[i - 1]);
            if (r >= lastR) {
                g[i] = mod.mul(2, g(i - 1));
            } else {
                g[i] = mod.mul(2, mod.plus(f(i - 1, r), g(i - 1)));
            }
        }
        return g[i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = in.readInt();
        }

        IntList list = new IntList();
        list.addAll(heights);
        list.add(0);
        dm = new DiscreteMap(list.toArray());
        m = dm.maxRank();

        f = new int[n][m + 1];
        g = new int[n];
        SequenceUtils.deepFill(f, -1);
        SequenceUtils.deepFill(g, -1);
        g[0] = 2;
        for (int i = dm.minRank(), r = dm.rankOf(heights[0]); i <= r; i++) {
            if (i == r) {
                f[0][i] = 0;
            } else if (dm.iThElement(i) == 0) {
                f[0][i] = mod.mul(1, mod.subtract(pow.pow(2, heights[0] - dm.iThElement(i)), 2));
            } else {
                f[0][i] = mod.mul(2, mod.subtract(pow.pow(2, heights[0] - dm.iThElement(i)), 1));
            }
        }

        int ans = mod.plus(f(n - 1, 0), g(n - 1));
        out.println(ans);
    }
}
