package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ConsecutiveAdding {
    void solve(long[] src, long[] target, int L) {
        int n = src.length;
        long[] delta = new long[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (i - L >= 0) {
                sum -= delta[i - L];
            }
            src[i] += sum;
            if (i + L - 1 < n) {
                delta[i] = target[i] - src[i];
                src[i] = target[i];
            }
            sum += delta[i];
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.ri();
        int c = in.ri();
        int x = in.ri();
        long[][] A = new long[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                A[i][j] = in.ri();
            }
        }
        long[][] B = new long[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                B[i][j] = in.ri();
            }
        }
        long[] target = new long[r];
        long[] src = new long[r];
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                src[j] = A[j][i];
                target[j] = B[j][i];
            }
            solve(src, target, x);
            for (int j = 0; j < r; j++) {
                A[j][i] = src[j];
            }
        }

        target = new long[c];
        src = new long[c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                src[j] = A[i][j];
                target[j] = B[i][j];
            }
            solve(src, target, x);
            for (int j = 0; j < c; j++) {
                A[i][j] = src[j];
            }
        }
        for(int i = 0; i < r; i++){
            for(int j = 0; j < c; j++){
                if(A[i][j] != B[i][j]){
                    out.println("No");
                    return;
                }
            }
        }
        out.println("Yes");
    }
}
