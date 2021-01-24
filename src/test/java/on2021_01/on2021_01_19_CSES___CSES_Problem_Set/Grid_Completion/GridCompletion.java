package on2021_01.on2021_01_19_CSES___CSES_Problem_Set.Grid_Completion;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Power;
import template.utils.Debug;

public class GridCompletion {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    Factorial fact = new Factorial(1000, mod);
    Combination comb = new Combination(fact);

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = 0;
        int b = 0;
        int c = 0;
        int b1 = 0;
        int c1 = 0;
        int d = 0;
        char[][] mat = new char[n][n];
        boolean[] asA = new boolean[n];
        boolean[] asB = new boolean[n];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 'A') {
                    asA[j] = true;
                }
                if (mat[i][j] == 'B') {
                    asB[j] = true;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (!asA[i] && !asB[i]) {
                d++;
            }
        }
        for (int i = 0; i < n; i++) {
            int A = -1;
            int B = -1;
            for (int j = 0; j < n; j++) {
                char ch = mat[i][j];
                if (ch == 'A') {
                    A = j;
                }
                if (ch == 'B') {
                    B = j;
                }
            }
            if (A >= 0 && B >= 0) {
                a++;
            } else if (A >= 0) {
                b++;
                if (!asB[A]) {
                    b1++;
                }
            } else if (B >= 0) {
                c++;
                if (!asA[B]) {
                    c1++;
                }
            }
        }
        debug.debug("a", a);
        debug.debug("b", b);
        debug.debug("c", c);

        long sum = 0;
        for (int x = 0; x <= b1; x++) {
            for (int y = 0; y <= d; y++) {
                for (int z = 0; z <= c1; z++) {
                    if (n - a - b - c - y < 0) {
                        continue;
                    }
                    long contrib = (long) comb.combination(n - a - c - y - x, b - x)
                            * fact.fact(b - x) % mod
                            * comb.combination(n - a - b - y - z, c - z) % mod
                            * fact.fact(c - z) % mod
                            * fact.fact(n - a - b - c - y) % mod
                            * comb.combination(b1, x) % mod
                            * comb.combination(d, y) % mod
                            * comb.combination(c1, z) % mod;
                    if ((x + y + z) % 2 == 1) {
                        contrib = -contrib;
                    }
                    sum += contrib;
                }
            }
        }
        debug.debug("sum", sum % mod);
        sum = sum % mod * fact.fact(n - a - b - c) % mod;
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
