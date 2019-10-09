package template;

import java.util.Arrays;

public class XorGuassianElimination {
    int[][] mat;
    int[] solutions;
    int rank;

    public XorGuassianElimination(int n, int m) {
        mat = new int[n + 1][m + 1];
        solutions = mat[n];
    }

    public void clear() {
        for (int[] row : mat) {
            Arrays.fill(row, 0);
        }
    }

    public void swapRow(int i, int j) {
        int[] tmp = mat[i];
        mat[i] = mat[j];
        mat[j] = tmp;
    }

    public void setRight(int row, int val) {
        mat[row][mat[row].length - 1] = val;
    }

    /**
     * Let a[i] = a[i] ^ a[j]
     */
    public void xorRow(int i, int j) {
        int m = mat[0].length;
        for (int k = 0; k < m; k++) {
            mat[i][k] ^= mat[j][k];
        }
    }

    public boolean solve() {
        int n = mat.length - 1;
        int m = mat[0].length - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] &= 1;
            }
        }
        int now = 0;
        for (int i = 0; i < n; i++) {
            int swapRow = -1;
            for (int j = now; j < m; j++) {
                if (mat[j][i] != 0) {
                    swapRow = j;
                    break;
                }
            }
            if (swapRow == -1) {
                continue;
            }
            swapRow(now, swapRow);
            for (int j = now + 1; j < m; j++) {
                if (mat[j][i] == 1) {
                    xorRow(j, now);
                }
            }
            now++;
        }

        for (int i = now; i < n; i++) {
            if (mat[i][m] != 0) {
                return false;
            }
        }

        rank = now;
        for (int i = now - 1; i >= 0; i--) {
            int x = -1;
            for (int j = 0; j < m; j++) {
                if (mat[i][j] != 0) {
                    x = j;
                    break;
                }
            }
            mat[n][x] = mat[i][m];
            for (int j = i - 1; j >= 0; j--) {
                if (mat[j][x] == 0) {
                    continue;
                }
                mat[j][x] = 0;
                mat[j][m] ^= mat[n][x];
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int n = mat.length - 1;
        int m = mat[0].length - 1;
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                row.append("x").append(j).append('+');
            }
            if (row.length() > 0) {
                row.setLength(row.length() - 1);
            } else {
                row.append(0);
            }
            row.append("=").append(mat[i][m]);
            builder.append(row).append('\n');
        }
        return builder.toString();
    }
}