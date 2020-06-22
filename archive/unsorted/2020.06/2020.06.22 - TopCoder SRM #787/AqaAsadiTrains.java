package contest;

import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class AqaAsadiTrains {
    int[] C;
    int[] A;
    int[] T;
    int[][][] f;
    int inf = (int) 1e8;

    public int[] f(int l, int r) {
        if (f[l][r] == null) {
            f[l][r] = new int[C.length + 1];
            Arrays.fill(f[l][r], -inf);
            if (l == r) {
                f[l][r][A[l]] = 0;
            }
            for (int i = 0; i <= C.length; i++) {
                for (int j = 0; j <= C.length; j++) {
                    int k = transform(i, j);
                    for (int t = l; t < r; t++) {
                        f[l][r][k] = Math.max(f[l][r][k], f(l, t)[i] + f(t + 1, r)[j]);
                        f[l][r][k] = Math.max(f[l][r][k], f(l, t)[j] + f(t + 1, r)[i]);
                    }
                }
            }

            for (int i = 0; i < C.length; i++) {
                f[l][r][C.length] = Math.max(f[l][r][C.length], f[l][r][i] + C[i]);
            }
        }
        return f[l][r];
    }

    //Debug debug = new Debug(false);
    public int getMax(int[] C, int[] A, int[] T) {
        this.C = C;
        this.A = A;
        this.T = T;
        f = new int[A.length][A.length][];

        int[] dp = new int[A.length + 1];
        Arrays.fill(dp, -inf);
        dp[0] = 0;
        for (int i = 1; i <= A.length; i++) {
            dp[i] = Math.max(dp[i], dp[i - 1]);
            for (int j = 0; j < i; j++) {
                dp[i] = Math.max(dp[i], dp[j] + f(j, i - 1)[C.length]);
            }
        }

        //debug.debug("dp", dp);

        return dp[A.length];
    }


    public int transform(int i, int j) {
        if (i >= C.length) {
            return j;
        }
        if (j >= C.length) {
            return i;
        }
        return T[i * C.length + j];
    }
}
