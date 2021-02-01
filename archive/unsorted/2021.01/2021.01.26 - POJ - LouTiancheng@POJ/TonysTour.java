package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.CountGridHamiltonPath;

import java.util.Arrays;

public class TonysTour {
    CountGridHamiltonPath solver = new CountGridHamiltonPath();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        if (n == 0 && m == 0) {
            throw new UnknownError();
        }
        boolean[][] mat = new boolean[n + 2][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc() == '.';
            }
        }
        mat[n][0] = mat[n][m - 1] = true;
        Arrays.fill(mat[n + 1], true);
        long ans = solver.count(mat);
        out.println(ans);
    }
}
