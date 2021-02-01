package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.problem.CountGridHamiltonPath;
import template.utils.Debug;

import java.util.Arrays;

public class Formula1 {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[][] mat = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc() == '.';
            }
        }
        long ans = new CountGridHamiltonPath().count(mat);
        out.println(ans);
    }
}
