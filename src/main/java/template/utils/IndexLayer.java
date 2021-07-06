package template.utils;

public class IndexLayer {
    public int[] indices;
    private int[] casted;
    private int[][] resMat;
    private int[][] T;
    public int[][] mat;

    int n;

    public IndexLayer(int n) {
        n++;
        this.n = n;
        mat = new int[n][n];
        resMat = new int[n][n];
        T = new int[n][n];
        indices = new int[n];
        casted = new int[n];
        asStandard(mat);
    }

    public void init() {
        asStandard(mat);
    }

    public void transform() {
        indices[n - 1] = 1;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += indices[j] * mat[i][j];
            }
            casted[i] = sum;
        }
        int[] tmp = indices;
        indices = casted;
        casted = tmp;
    }

    private void rightMul() {
        SequenceUtils.deepFill(resMat, 0);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    resMat[i][j] += mat[i][k] * T[k][j];
                }
            }
        }
        int[][] tmp = resMat;
        resMat = mat;
        mat = tmp;
    }

    public void permutation(int... p) {
        SequenceUtils.deepFill(T, 0);
        for (int i = 0; i < n - 1; i++) {
            T[i][p[i]] = 1;
        }
        T[n - 1][n - 1] = 1;
        rightMul();
    }

    private void asStandard(int[][] M) {
        SequenceUtils.deepFill(M, 0);
        for (int i = 0; i < n; i++) {
            M[i][i] = 1;
        }
    }

    public void affine(int i, int a, int b) {
        asStandard(T);
        T[i][i] = a;
        T[i][n - 1] = b;
        rightMul();
    }
}
