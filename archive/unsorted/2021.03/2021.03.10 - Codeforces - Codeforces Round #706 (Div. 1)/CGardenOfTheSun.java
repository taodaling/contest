package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CGardenOfTheSun {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        boolean[] hasAny = new boolean[n];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 'X') {
                    hasAny[i] = true;
                }
            }
        }
        int row = 0;
        int lastRow = -10;
        while (row < n) {
            if (row - lastRow == 2 && hasAny[row - 1]) {
                if (row == n - 1) {
                    for (int j = 0; j < m; j++) {
                        if (mat[row][j] == 'X') {
                            mat[row - 1][j] = 'X';
                        }
                    }
                    break;
                } else {
                    row++;
                    continue;
                }
            }

            for (int j = 0; j < m; j++) {
                mat[row][j] = 'X';
            }
            if (lastRow >= 0) {
                if (row - lastRow == 2) {
                    mat[lastRow + 1][0] = 'X';
                } else if (row - lastRow == 3) {
                    for (int j = 0; j < m; j++) {
                        if (mat[row - 2][j] == 'X') {
                            mat[row - 1][j] = 'X';
                            break;
                        }
                    }
                }else {
                    assert false;
                }
            }
            lastRow = row;
            row += 2;
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(mat[i][j]);
            }
            out.println();
        }
        out.println();
    }
}
