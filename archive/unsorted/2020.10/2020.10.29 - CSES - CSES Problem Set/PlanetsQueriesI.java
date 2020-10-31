package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class PlanetsQueriesI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[][] dest = new int[30][n];
        for (int i = 0; i < n; i++) {
            dest[0][i] = in.readInt() - 1;
        }
        for (int i = 0; i < 30 - 1; i++) {
            for (int j = 0; j < n; j++) {
                dest[i + 1][j] = dest[i][dest[i][j]];
            }
        }
        for (int i = 0; i < q; i++) {
            int x = in.readInt() - 1;
            int k = in.readInt();
            for (int j = 30 - 1; j >= 0; j--) {
                if (k >= 1 << j) {
                    k -= 1 << j;
                    x = dest[j][x];
                }
            }
            out.println(x + 1);
        }
    }
}
