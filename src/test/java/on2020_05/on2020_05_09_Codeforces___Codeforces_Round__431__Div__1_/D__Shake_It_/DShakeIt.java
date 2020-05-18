package on2020_05.on2020_05_09_Codeforces___Codeforces_Round__431__Div__1_.D__Shake_It_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeInverseNumber;
import template.math.Modular;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DShakeIt {
    Modular mod = new Modular(1e9 + 7);
    // Factorial fact = new Factorial(100, mod);
    // Combination comb = new Combination(fact);
    int[][][][] h;
    int[][] g;
    int n;
    int flow;

    ModPrimeInverseNumber inv = new ModPrimeInverseNumber(100, mod);

    Debug debug = new Debug(false);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        flow = Math.max(n + 1, m);

        // f = new int[n + 1][flow + 1];
        h = new int[n + 1][flow + 1][n + 1][flow + 1];
        g = new int[n + 1][flow + 1];

        //SequenceUtils.deepFill(f, -1);
        SequenceUtils.deepFill(g, -1);
        SequenceUtils.deepFill(h, -1);

        //g(2, 0);

        int ans = f(n, m - 1);
        //ans = mod.mul(ans, fact.invFact(n));

        //debug.debug("f", f);
        debug.debug("g", g);
        debug.debug("h", h);
        out.println(ans);
    }

    public int h(int i, int j, int a, int b) {
        if (i < 0 || j < 0) {
            return 0;
        }
        if (h[i][j][a][b] == -1) {
            h[i][j][a][b] = 0;
            if (a == 0 || b == 0) {
                return h[i][j][a][b] = i == 0 && j == 0 ? 1 : 0;
            }
            int limit = Math.min(i / a, j / b);
            int prod = 1;
            int aa = a;
            int bb = b;
            if (bb > 1) {
                bb--;
            } else {
                bb = flow;
                aa--;
            }
            h[i][j][a][b] = h(i, j, aa, bb);
            for (int t = 1; t <= limit; t++) {
                prod = mod.mul(prod, g(a - 1, b - 1) + t - 1);
                prod = mod.mul(prod, inv.inverse(t));
                int local = mod.mul(h(i - a * t, j - b * t, aa, bb),
                        prod);
                h[i][j][a][b] = mod.plus(h[i][j][a][b], local);
            }

        }

        return h[i][j][a][b];
    }

    public int f(int i, int j) {
        return h(i, j, i, j);
    }


    public int g(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }
        if (g[i][j] == -1) {
            g[i][j] = 0;
            if (i == 0) {
                return g[i][j] = j == 0 ? 1 : 0;
            }
            for (int a = 0; a <= i; a++) {
                int c = i - a;
                for (int b = j; b <= a + 1; b++) {
                    for (int d = j; d <= c + 1; d++) {
                        if (Math.min(b, d) != j) {
                            continue;
                        }
                        int way = mod.mul(f(a, b), f(c, d));

                        g[i][j] = mod.plus(g[i][j], way);
                    }
                }
            }
        }

        return g[i][j];
    }
}
