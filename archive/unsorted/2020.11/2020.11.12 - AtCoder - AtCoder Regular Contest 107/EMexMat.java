package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class EMexMat {
    public void output(long[] cnts, FastOutput out){
        for(long x : cnts){
            out.append(x).append(' ');
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][];
        for (int i = 0; i < n; i++) {
            if (i < 4) {
                mat[i] = new int[n];
            }else{
                mat[i] = new int[4];
            }
        }
        for (int i = 0; i < n; i++) {
            mat[0][i] = in.readInt();
        }
        for (int i = 1; i < n; i++) {
            mat[i][0] = in.readInt();
        }
        if (n <= 4) {
            long[] cnts = bf(mat);
            output(cnts, out);
            return;
        }
        for (int i = 1; i < n; i++) {
            int r = i < 4 ? n : 4;
            for(int j = 1; j < r; j++){
                mat[i][j] = mex(mat[i - 1][j], mat[i][j - 1]);
            }
        }
        long[] cnts = new long[3];
        for (int[] row : mat) {
            for (int x : row) {
                cnts[x]++;
            }
        }
        for (int i = 3; i < n; i++) {
            int diag = Math.min(n - i - 1, n - 4);
            cnts[mat[3][i]] += diag;
        }
        for (int i = 4; i < n; i++) {
            int diag = Math.min(n - i - 1, n - 4);
            cnts[mat[i][3]] += diag;
        }
        debug.debugMatrix("mat", mat);
        assert Arrays.equals(bf(mat), cnts);
        out.append(cnts[0]).append(' ').append(cnts[1]).append(' ')
                .append(cnts[2]);
    }

    public long[] bf(int[][] mat) {
        int n = mat.length;
        int[][] trans = new int[n][n];
        for (int i = 0; i < n; i++) {
            trans[i][0] = mat[i][0];
            trans[0][i] = mat[0][i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                trans[i][j] = mex(trans[i - 1][j], trans[i][j - 1]);
            }
        }
        long[] cnts = new long[3];
        for (int[] row : trans) {
            for (int cell : row) {
                cnts[cell]++;
            }
        }
        debug.debugMatrix("trans", trans);
        return cnts;
    }

    Debug debug = new Debug(false);

    public int mex(int a, int b) {
        if (a != 0 && b != 0) {
            return 0;
        }
        if (a != 1 && b != 1) {
            return 1;
        }
        return 2;
    }
}
