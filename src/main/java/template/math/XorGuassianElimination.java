package template.math;

import java.util.Arrays;

public class XorGuassianElimination {
    int[][] mat;
    int[] solutions;
    int rank;
    int n;
    int m;
    int start;

    public int rank() {
        return rank;
    }

    public XorGuassianElimination(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new int[n + 1][m + 1];
        solutions = mat[n];
    }

    public void clear(int n, int m) {
        this.n = n;
        this.m = m;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                mat[i][j] = 0;
            }
        }
        solutions = mat[n];
    }

    public void setRight(int row, int val) {
        mat[row][m] = val;
    }

    public void setLeft(int row, int col, int val) {
        mat[row][col] = val;
    }

    public int getLeft(int row, int col) {
        return mat[row][col];
    }

    public int getRight(int row) {
        return mat[row][m];
    }

    public void modifyLeft(int row, int col, int val) {
        mat[row][col] = mat[row][col] ^ val;
    }


    public void modifyRight(int row, int val) {
        mat[row][m] = mat[row][m] ^ val;
    }

    public int[] getSolutions() {
        return solutions;
    }

    /**
     * O(nm^2)
     * @return
     */
    public boolean solve() {
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                mat[i][j] = mat[i][j] & 1;
            }
        }

        int now = 0;
        for (int i = 0; i < m; i++) {
            start = i;
            int maxRow = now;
            for (int j = now; j < n; j++) {
                if (mat[j][i] != 0) {
                    maxRow = j;
                    break;
                }
            }

            if (mat[maxRow][i] == 0) {
                continue;
            }
            swapRow(now, maxRow);
            divideRow(now, mat[now][i]);
            for (int j = now + 1; j < n; j++) {
                if (mat[j][i] == 0) {
                    continue;
                }
                int f = mat[j][i];
                subtractRow(j, now, f);
            }

            now++;
        }

        rank = now;
        for (int i = now; i < n; i++) {
            if (mat[i][m] != 0) {
                return false;
            }
        }

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
                mat[j][m] = mat[j][m] ^ mat[n][x];
                mat[j][x] = 0;
            }
        }
        return true;
    }

    void swapRow(int i, int j) {
        int[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j, int f) {
        for (int k = start; k <= m; k++) {
            mat[i][k] = mat[i][k] ^ (mat[j][k] & f);
        }
    }

    void divideRow(int i, int f) {
        int divisor = f;
        for (int k = start; k <= m; k++) {
            mat[i][k] = mat[i][k] & divisor;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                if (mat[i][j] != 1) {
                    row.append(mat[i][j]);
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