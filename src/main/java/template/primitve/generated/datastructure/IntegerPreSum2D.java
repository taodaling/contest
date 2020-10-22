package template.primitve.generated.datastructure;

public class IntegerPreSum2D {
    int[][] ps;

    public IntegerPreSum2D(int n, int m) {
        ps = new int[n + 1][m + 1];
    }

    public void init(Int2ToIntegerFunction func, int n, int m) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                ps[i][j] = func.apply(i - 1, j - 1);
                ps[i][j] += ps[i][j - 1];
            }
            for (int j = 1; j <= m; j++) {
                ps[i][j] += ps[i - 1][j];
            }
        }
    }

    int rect(int low, int hi, int l, int r) {
        return ps[hi + 1][r + 1] - ps[low][r + 1] - ps[hi + 1][l] + ps[low][l];
    }
}
