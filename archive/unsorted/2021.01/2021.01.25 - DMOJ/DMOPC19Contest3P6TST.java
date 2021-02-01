package contest;

import template.algo.MatroidIndependentSet;
import template.algo.MatroidPartition;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;

public class DMOPC19Contest3P6TST {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[m];
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.ri() - 1;
            b[i] = in.ri() - 1;
        }
        MatroidIndependentSet set = MatroidIndependentSet.ofSpanningTree(n, new int[][]{a, b});
        Pair<int[], Integer> ans = new MatroidPartition(m).solve(set, 3);
        int[] cnts = new int[4];
        for (int x : ans.a) {
            cnts[x + 1]++;
        }
        for (int i = 1; i <= 3; i++) {
            if (cnts[i] != n - 1) {
                out.println(-1);
                return;
            }
        }
        for (int i = 0; i < m; i++) {
            out.append(ans.a[i] + 1);
        }
    }
}
