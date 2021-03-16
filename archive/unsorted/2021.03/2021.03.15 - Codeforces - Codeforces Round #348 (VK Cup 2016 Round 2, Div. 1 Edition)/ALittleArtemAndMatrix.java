package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ALittleArtemAndMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        int[][] mat = new int[n][m];
        int[][] ops = new int[q][];
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 3) {
                ops[i] = new int[]{t, in.ri(), in.ri(), in.ri()};
            } else {
                ops[i] = new int[]{t, in.ri()};
            }
        }
        SequenceUtils.reverse(ops);
        for (int[] op : ops) {
            int t = op[0];
            if (t == 3) {
                int x = op[1] - 1;
                int y = op[2] - 1;
                mat[x][y] = op[3];
            } else if (t == 1) {
                int r = op[1] - 1;
                int first = mat[r][m - 1];
                for (int j = m - 1; j >= 1; j--) {
                    mat[r][j] = mat[r][j - 1];
                }
                mat[r][0] = first;
            } else {
                int c = op[1] - 1;
                int first = mat[n - 1][c];
                for (int j = n - 1; j >= 1; j--) {
                    mat[j][c] = mat[j - 1][c];
                }
                mat[0][c] = first;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(mat[i][j]).append(' ');
            }
            out.println();
        }
    }
}
