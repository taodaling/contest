package on2020_05.on2020_05_30_AtCoder___AtCoder_Grand_Contest_020.E___Encoding_Subsets;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.SequenceUtils;

import java.util.BitSet;

public class EEncodingSubsets {
    Modular mod = new Modular(998244353);
    int[][] g;
    int[][] f;
    int[][][] wayToAssign;

    public int f(int l, int r) {
        return -1;
    }

    public int g(int l, int r) {
        if (l >= r) {
            return 0;
        }
        if (g[l][r] == -1) {
            g[l][r] = 0;
            int len = r - l + 1;
            for (int d = 2; d <= len / 2; d++) {
                if (len % d != 0) {
                    continue;
                }
            }
        }
        return -1;
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[100];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        int[][] nextZero = new int[n][n + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= n; j++) {
                if (s[i] == 0) {
                    nextZero[i][j] = 0;
                } else {
                    nextZero[i][j] = i + j >= n ? 1 : (nextZero[i][i + j] + 1);
                }
            }
        }

        //i, j, k
        wayToAssign = new int[n][n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 1; k <= n; k++) {
                    if (i + (j + 1) * k - 1 >= n) {
                        continue;
                    }
                    wayToAssign[i][j][k] = 1;
                    for (int t = i; t <= i + j; t++) {
                        if (nextZero[i][j + 1] >= k) {
                            wayToAssign[i][j][k] = mod.mul(wayToAssign[i][j][k], 2);
                        }
                    }
                }
            }
        }

        g = new int[n][n];
        SequenceUtils.deepFill(g, -1);
        f = new int[n][n];
        SequenceUtils.deepFill(f, -1);

    }
}
