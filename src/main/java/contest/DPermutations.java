package contest;

import template.datastructure.BitSet;
import template.datastructure.GenericLinearBasis;
import template.io.FastInput;
import template.io.FastOutput;

public class DPermutations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] x = new int[m];
        int[] y = new int[m];
        boolean[][] retain = new boolean[n][n];
        BitSet[] rows = new BitSet[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new BitSet(n);
        }
        for (int i = 0; i < m; i++) {
            x[i] = in.readInt() - 1;
            y[i] = in.readInt() - 1;
            rows[x[i]].set(y[i]);
        }

        GenericLinearBasis lb = new GenericLinearBasis(n);
        for (int i = 0; i < n; i++) {
            lb.clear();
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                lb.add(rows[j]);
            }
            for (int j = rows[i].nextSetBit(0); j < rows[i].capacity();
                 j = rows[i].nextSetBit(j + 1)) {
                rows[i].clear(j);
                if (!lb.test(rows[i])) {
                    retain[i][j] = true;
                }
                rows[i].set(j);
            }
        }

        for(int i = 0; i < m; i++){
            out.println(retain[x[i]][y[i]] ? "YES" : "NO");
        }
    }
}
