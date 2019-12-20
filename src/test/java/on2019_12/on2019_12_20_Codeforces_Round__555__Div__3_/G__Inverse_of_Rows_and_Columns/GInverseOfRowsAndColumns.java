package on2019_12.on2019_12_20_Codeforces_Round__555__Div__3_.G__Inverse_of_Rows_and_Columns;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class GInverseOfRowsAndColumns {
    int n;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();

        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readInt();
            }
        }

        int[] colMask = new int[m];
        int[] rowMask = new int[n];

        for (int k = 0; k <= 1; k++) {
            rowMask[0] = k;

            //all one
            Arrays.fill(colMask, 0);
            Arrays.fill(rowMask, 1, n, 0);
            for (int i = 0; i < m; i++) {
                colMask[i] = 1 ^ mat[0][i] ^ rowMask[0];
            }
            if (flip(mat, rowMask, colMask, 1)) {
                yes(out, rowMask, colMask);
                return;
            }

            for (int i = 0; i < m; i++) {
                Arrays.fill(colMask, 0);
                Arrays.fill(rowMask, 1, n, 0);
                for (int j = 0; j < m; j++) {
                    if (j > i) {
                        colMask[j] = 1 ^ mat[0][j] ^ rowMask[0];
                    } else {
                        colMask[j] = mat[0][j] ^ rowMask[0];
                    }
                }
                if (flip(mat, rowMask, colMask, 1)) {
                    yes(out, rowMask, colMask);
                    return;
                }
            }
        }

        out.println("NO");
    }

    public void yes(FastOutput out, int[] rowMask, int[] colMask) {
        out.println("YES");
        for (int x : rowMask) {
            out.append(x);
        }
        out.println();
        for (int x : colMask) {
            out.append(x);
        }
        out.println();
    }

    public boolean check(int last, int rowMask, int[] colMask, int[] row) {
        for (int i = 0; i < m; i++) {
            if (last > (row[i] ^ rowMask ^ colMask[i])) {
                return false;
            }
            last = (row[i] ^ rowMask ^ colMask[i]);
        }
        return true;
    }

    public boolean flip(int[][] mat, int[] rowMask, int[] colMask, int i) {
        if (i == mat.length) {
            return true;
        }

        int lastElement = mat[i - 1][m - 1] ^ rowMask[i - 1] ^ colMask[m - 1];

        rowMask[i] = lastElement ^ mat[i][0];
        if (!check(lastElement, rowMask[i], colMask, mat[i])) {
            rowMask[i] ^= 1;
            if (!check(lastElement, rowMask[i], colMask, mat[i])) {
                return false;
            }
        }

        return flip(mat, rowMask, colMask, i + 1);
    }
}
