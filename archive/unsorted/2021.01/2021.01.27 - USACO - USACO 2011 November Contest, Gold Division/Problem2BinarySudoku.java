package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Problem2BinarySudoku {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[][] mat = new int[9][9];
        int[] row = new int[9];
        int[] col = new int[9];
        int[][] block = new int[3][3];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                mat[i][j] = in.rc() - '0';
                row[i] ^= mat[i][j];
                col[j] ^= mat[i][j];
                block[i / 3][j / 3] ^= mat[i][j];
            }
        }
        int[] rb = new int[3];
        int[] cb = new int[3];
        for (int i = 0; i < 9; i++) {
            rb[i / 3] += row[i];
            cb[i / 3] += col[i];
        }
        for (int i = 0; i < 3; i++) {
            int csum = 0;
            int rsum = 0;
            for (int j = 0; j < 3; j++) {
                rsum += block[i][j];
                csum += block[j][i];
            }
            rb[i] = Math.max(rb[i], rsum);
            cb[i] = Math.max(cb[i], csum);
        }
        long ans = Math.max(Arrays.stream(rb).sum(), Arrays.stream(cb).sum());
        out.println(ans);
    }
}
