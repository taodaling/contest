package template.math;

import java.util.Arrays;

public class GuassianElimination {
    double[][] mat;
    int rank;
    final double prec;
    int n;
    int m;
    boolean[] independent;
    double[] solutions;

    public GuassianElimination(int n, int m, double prec) {
        this.prec = prec;
        this.n = n;
        this.m = m;
        mat = new double[n + 1][m + 1];
        solutions = mat[n];
        independent = new boolean[m];
    }

    public double[][] getMat() {
        return mat;
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
        Arrays.fill(independent, false);
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

            if (Math.abs(mat[maxRow][i]) <= prec) {
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
            if (Math.abs(mat[i][m]) > prec) {
                return false;
            }
        }

        rank = now;
        for (int i = now - 1; i >= 0; i--) {
            int x = -1;
            for (int j = 0; j < m; j++) {
                if (Math.abs(mat[i][j]) > prec) {
                    x = j;
                    break;
                }
            }
            mat[n][x] = mat[i][m] / mat[i][x];
            independent[x] = true;
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

    public int getRank() {
        return rank;
    }

    public double[] getSolutions(){
        return solutions;
    }

    public boolean[] getIndependent() {
        return independent;
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