package contest;




import template.algo.MatroidIndependentSet;
import template.algo.MatroidPartition;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[m];
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.ri();
            b[i] = in.ri();
        }
        MatroidIndependentSet set = MatroidIndependentSet.ofSpanningTree(n, new int[][]{a, b});
        MatroidPartition partition = new MatroidPartition(m);
        Pair<int[], Integer> ans = partition.solve(set, 3);
        int[] type = ans.a;
        for (int i = 0; i < 3; i++) {
            int cnt = 0;
            for (int j = 0; j < m; j++) {
                if (type[j] != i) {
                    continue;
                }
                cnt++;
                out.append(j).append(' ');
            }
            assert cnt == n - 1;
            out.println();
        }
    }
}
