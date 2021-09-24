package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Flip_Game;



import template.io.FastInput;
import template.io.FastOutput;

public class FlipGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
            }
        }

        long sum0 = sum(deepClone(mat), n, m);
        flipCol(mat, 0);
        long sum1 = sum(mat, n, m);
        long ans = Math.max(sum0, sum1);
        out.println(ans);
    }

    public int[][] deepClone(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] ans = new int[n][];
        for (int j = 0; j < n; j++) {
            ans[j] = mat[j].clone();
        }
        return ans;
    }

    public void flipRow(int[][] mat, int r) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < m; i++) {
            mat[r][i] ^= 1;
        }
    }


    public void flipCol(int[][] mat, int c) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            mat[i][c] ^= 1;
        }
    }

    public long sum(int[][] mat, int n, int m) {
        for (int i = 0; i < n; i++) {
            if (mat[i][0] == 0) {
                flipRow(mat, i);
            }
        }
        for (int i = 1; i < m; i++) {
            int one = 0;
            for (int j = 0; j < n; j++) {
                one += mat[j][i];
            }
            if (n - one > one) {
                flipCol(mat, i);
            }
        }
        long sum = 0;
        for (int i = 0; i < n; i++) {
            long v = 0;
            for (int j = 0; j < m; j++) {
                v <<= 1;
                v |= mat[i][j];
            }
            sum += v;
        }
        return sum;
    }
}
