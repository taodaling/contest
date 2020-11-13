package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT2D;

public class ForestQueriesII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        IntegerBIT2D bit = new IntegerBIT2D(n, n);
        int[][] mat = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (in.readChar() == '*') {
                    bit.update(i, j, 1);
                    mat[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < q; i++) {
            if (in.readInt() == 1) {
                int x = in.readInt();
                int y = in.readInt();
                mat[x][y] ^= 1;
                if (mat[x][y] == 0) {
                    bit.update(x, y, -1);
                } else {
                    bit.update(x, y, 1);
                }
            } else {
                int b = in.readInt();
                int l = in.readInt();
                int t = in.readInt();
                int r = in.readInt();
                int ans = bit.rect(b, l, t, r);
                out.println(ans);
            }
        }
    }
}
