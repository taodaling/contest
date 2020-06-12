package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Factorization;
import template.math.Modular;
import template.math.Power;
import template.polynomial.NumberTheoryTransform;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DDivisionGame {
    Modular mod = new Modular(985661441);
    Power power = new Power(mod);
    Factorial fact = new Factorial(1000000, mod);
    Combination comb = new Combination(fact);
    NumberTheoryTransform ntt = new NumberTheoryTransform(mod);

    int[] seqA = new int[1 << 20];
    int[] seqB = new int[1 << 20];
    int[] seqC = new int[1 << 20];
    //Debug debug = new Debug(true);

    public int way(int n, int m) {
        return comb.combination(n + m - 1, n);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        int m = in.readInt();
        int k = in.readInt();
        int[] a = new int[m];
        int sum = 0;
        for (int i = 0; i < m; i++) {
            in.readInt();
            a[i] = in.readInt();
            sum += a[i];
        }
        int proper = Log2.ceilLog(sum + 1 + sum + 1 + 1);
        for (int i = 0; i <= sum + 1; i++) {
            seqA[i] = fact.invFact(i);
            if ((i & 1) == 1) {
                seqA[i] = mod.valueOf(-seqA[i]);
            }
            seqB[i] = fact.invFact(i);
            for (int j = 0; j < m; j++) {
                seqB[i] = mod.mul(seqB[i], way(a[j], i));
            }
        }
        for (int i = sum + 2; i < 1 << proper; i++) {
            seqA[i] = seqB[i] = 0;
        }

        ntt.dft(seqA, proper);
        ntt.dft(seqB, proper);
        ntt.dotMul(seqA, seqB, seqC, proper);
        ntt.idft(seqC, proper);

        int[] f = seqC;
        for (int i = 0; i <= sum; i++) {
            f[i] = mod.mul(f[i + 1], fact.fact(i + 1));
        }

        //debug.debug("f", Arrays.copyOf(f, sum + 1));
        int[][] powF = new int[2][k + 1];
        int[] ans = new int[k];
        for (int i = 0; i <= k; i++) {
            powF[0][i] = i == 0 ? 1 : mod.mul(powF[0][i - 1], f[0]);
        }
        for (int i = 1; i <= sum; i++) {
            for (int j = 0; j <= k; j++) {
                powF[1][j] = j == 0 ? 1 : mod.mul(powF[1][j - 1], f[i]);
            }

            for (int j = 0; j < k; j++) {
                int local = mod.mul(powF[1][j], powF[0][k - j]);
                ans[j] = mod.plus(ans[j], local);
            }

            SequenceUtils.swap(powF, 0, 1);
        }

        for (int x : ans) {
            out.append(x).append(' ');
        }

        out.println();
    }
}
