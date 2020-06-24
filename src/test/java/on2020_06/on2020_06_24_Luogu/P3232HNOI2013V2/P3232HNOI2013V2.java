package on2020_06.on2020_06_24_Luogu.P3232HNOI2013V2;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.math.Matrix;
import template.math.SparseMatrix;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class P3232HNOI2013V2 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        double[][] mat = new double[n][n];
        double[] vector = new double[n];
        vector[0] = 1;
        for (int i = 0; i < n; i++) {
            mat[i][i] = 1;
        }
        int[][] edges = new int[m][2];
        int[] deg = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[i][j] = in.readInt() - 1;
                deg[edges[i][j]]++;
            }
        }
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            if (u != n - 1) {
                mat[v][u] -= 1.0 / deg[u];
            }
            if (v != n - 1) {
                mat[u][v] -= 1.0 / deg[v];
            }
        }

        SparseMatrix A = new SparseMatrix(new Matrix(mat));
        //debug.debug("A", A);
        //Matrix invA = Matrix.inverse(A);
//        Matrix b = new Matrix(n, 1);
//        for (int i = 0; i < n; i++) {
//            b.set(i, 0, vector[i]);
//        }

        //Matrix x = Matrix.mul(invA, b);
        double[] x = A.solveLinearEquation(vector);

        double[] weight = new double[m];
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            if (u != n - 1) {
                weight[i] += x[u] / deg[u];
            }
            if (v != n - 1) {
                weight[i] += x[v] / deg[v];
            }
        }


        debug.debug("x", x);
        debug.debug("weight", weight);
        Randomized.shuffle(weight);
        Arrays.sort(weight);
        KahanSummation sum = new KahanSummation();
        for (int i = 0; i < m; i++) {
            sum.add((m - i) * weight[i]);
        }

        double ans = sum.sum();
        out.printf("%.3f", ans);
    }
}
