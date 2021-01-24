package contest;

import template.math.ModSparseMatrix;
import template.math.Power;
import template.string.AhoCorasick;
import template.utils.Debug;

import java.util.Arrays;

public class RollMe {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

//    Debug debug = new Debug(true);
    public int solve(int[] die, String goal) {
        int n = die.length;
        long[] prob = new long[n];
        int sum = Arrays.stream(die).sum();
        long invSum = pow.inverse(sum);
        for (int i = 0; i < n; i++) {
            prob[i] = die[i] * invSum % mod;
        }
//        debug.debugArray("prob", prob);
        AhoCorasick ac = new AhoCorasick('0', '0' + n - 1, goal.length());
        ac.prepareBuild();
        for (char c : goal.toCharArray()) {
            ac.build(c);
        }
        int[] topo = ac.endBuild();
        int m = topo.length;
        int[] right = new int[m];
        Arrays.fill(right, 0, m - 1, mod - 1);
        ModSparseMatrix matrix = new ModSparseMatrix(m, m + n * m);
        int allocIndicator = 0;
        for (int i = 0; i < m; i++) {
            matrix.set(allocIndicator++, i, i, mod - 1);
        }
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                int to = ac.next[j][i];
                int p = (int) prob[j];
                matrix.set(allocIndicator++, i, to, p);
            }
        }

        int[] left = matrix.solveLinearEquation(right, pow);
        return left[0];
    }
}
