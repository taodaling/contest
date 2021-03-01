package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BBatchSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri() - 1;
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                swapCol(mat, i, j);
                if (check(mat)) {
                    out.println("YES");
                    return;
                }
                swapCol(mat, i, j);
            }
        }
        out.println("NO");
    }

    public void swapCol(int[][] mat, int i, int j) {
        for (int[] row : mat) {
            SequenceUtils.swap(row, i, j);
        }
    }

    public boolean check(int[][] mat) {
        for (int[] row : mat) {
            int req = 0;
            for (int i = 0; i < row.length; i++) {
                if (row[i] == i) {
                    continue;
                }
                if (row[row[i]] != i) {
                    return false;
                }
                req++;
            }
            if(req / 2 > 1){
                return false;
            }
        }
        return true;
    }
}
