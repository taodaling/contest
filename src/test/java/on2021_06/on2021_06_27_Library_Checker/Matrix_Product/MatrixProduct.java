package on2021_06.on2021_06_27_Library_Checker.Matrix_Product;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;

public class MatrixProduct {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
//        int[][] A = new int[n][m];
//        int[][] B = new int[m][k];
        ModMatrix A = new ModMatrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
//                A[i][j] = in.ri();
                A.set(i, j, in.ri());
            }
        }
        ModMatrix B = new ModMatrix(m, k);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
//                B[i][j] = in.ri();
                B.set(i, j, in.ri());
            }
        }

//        long limit = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
//        long[][] ans = new long[n][k];
//        for (int i = 0; i < n; i++) {
//            for (int t = 0; t < m; t++) {
//                for (int j = 0; j < k; j++) {
//                    ans[i][j] += (long) A[i][t] * B[t][j];
//                    if (ans[i][j] >= limit) {
//                        ans[i][j] %= mod;
//                    }
//                }
//            }
//        }

        ModMatrix ans = ModMatrix.mul(A, B, mod);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                out.append(ans.get(i, j) % mod).append(' ');
            }
            out.println();
        }
    }

}
