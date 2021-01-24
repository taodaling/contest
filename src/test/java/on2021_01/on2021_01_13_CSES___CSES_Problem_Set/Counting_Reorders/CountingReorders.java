package on2021_01.on2021_01_13_CSES___CSES_Problem_Set.Counting_Reorders;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;

import java.util.Arrays;

public class CountingReorders {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e4, mod);
    Combination comb = new Combination((int) 1e4, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[5000];
        int n = in.rs(s);
        int charset = 'z' - 'a' + 1;
        int[] cnts = new int[charset];
        for (char c : Arrays.copyOf(s, n)) {
            cnts[c - 'a']++;
        }
        int[][] polys = new int[charset][];
        for (int i = 0; i < charset; i++) {
            int m = cnts[i];
            if (m == 0) {
                polys[i] = new int[]{1};
                continue;
            }
            polys[i] = new int[m];
            for (int j = 0; j < m; j++) {
                long v = (long) fact.invFact(m - j) * comb.combination(m - 1, j) % mod;
                if (j % 2 == 1) {
                    v = DigitUtils.negate((int) v, mod);
                }
                polys[i][j] = (int) v;
            }
        }
        IntPoly poly = new IntPolyFFT(mod);
        int[] prod = poly.dacMul(polys);
        long sum = 0;
        for (int i = 0; i < prod.length && i <= n; i++) {
            long contrib = (long) fact.fact(n - i) * prod[i] % mod;
            sum += contrib;
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
