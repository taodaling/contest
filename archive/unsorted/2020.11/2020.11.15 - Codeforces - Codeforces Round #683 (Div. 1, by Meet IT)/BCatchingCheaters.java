package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BCatchingCheaters {
    char[] a;
    char[] b;
    int[][] f;

    public int f(int i, int j) {
        if (i < 0 || j < 0) {
            return -(int) 1e9;
        }
        if (f[i][j] == -1) {
            f[i][j] = Math.max(f(i - 1, j), f(i, j - 1));
            if (a[i] == b[j]) {
                f[i][j] = Math.max(f[i][j], 4 + f(i - 1, j - 1));
                f[i][j] = Math.max(f[i][j], 4 + i + j);
            }
        }
        return f[i][j];
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        a = new char[n];
        b = new char[m];
        in.readString(a, 0);
        in.readString(b, 0);

        f = new int[n][m];
        SequenceUtils.deepFill(f, -1);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans = Math.max(ans, f(i, j) - (i + 1) - (j + 1));
            }
        }

        out.println(ans);
    }
}
