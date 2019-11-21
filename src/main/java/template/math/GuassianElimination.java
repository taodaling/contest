package template.math;

public class GuassianElimination {
    double[][] mat;
    double[] solutions;
    int rank;
    static final double PREC = 1e-8;
    int n;
    int m;

    public GuassianElimination(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new double[n + 1][m + 1];
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


    public void setRight(int row, double val) {
        mat[row][mat[row].length - 1] = val;
    }

    public boolean solve() {
        int n = mat.length - 1;
        int m = mat[0].length - 1;

        int now = 0;
        for (int i = 0; i < m; i++) {
            int maxRow = now;
            for (int j = now; j < n; j++) {
                if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
                    maxRow = j;
                }
            }

            if (Math.abs(mat[maxRow][i]) <= PREC) {
                continue;
            }
            swapRow(now, maxRow);
            divideRow(now, mat[now][i]);
            for (int j = now + 1; j < n; j++) {
                if (mat[j][i] == 0) {
                    continue;
                }
                double f = mat[j][i];
                subtractRow(j, now, f);
            }

            now++;
        }

        for (int i = now; i < n; i++) {
            if (Math.abs(mat[i][m]) > PREC) {
                return false;
            }
        }

        rank = now;
        for (int i = now - 1; i >= 0; i--) {
            int x = -1;
            for (int j = 0; j < m; j++) {
                if (Math.abs(mat[i][j]) > PREC) {
                    x = j;
                    break;
                }
            }
            mat[n][x] = mat[i][m] / mat[i][x];
            for (int j = i - 1; j >= 0; j--) {
                if (mat[j][x] == 0) {
                    continue;
                }
                mat[j][m] -= mat[j][x] * mat[n][x];
                mat[j][x] = 0;
            }
        }
        return true;
    }

    void swapRow(int i, int j) {
        double[] row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j, double f) {
        int m = mat[0].length;
        for (int k = 0; k < m; k++) {
            mat[i][k] -= mat[j][k] * f;
        }
    }

    void divideRow(int i, double f) {
        int m = mat[0].length;
        for (int k = 0; k < m; k++) {
            mat[i][k] /= f;
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