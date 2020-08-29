package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.primitve.generated.datastructure.IntegerSparseTable2D;
import template.primitve.generated.datastructure.LongSparseTable2D;

public class DAnimalsAndPuzzle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readInt();
            }
        }
        int[][] square = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 || j == 0) {
                    square[i][j] = mat[i][j];
                    continue;
                }
                if (mat[i][j] == 0) {
                    continue;
                }
                square[i][j] = 1 + Math.min(Math.min(square[i][j - 1], square[i - 1][j]), square[i - 1][j - 1]);
            }
        }

        st = new IntegerSparseTable2D((i, j) -> square[i][j], n, m, Math::max);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int b = in.readInt() - 1;
            int l = in.readInt() - 1;
            int t = in.readInt() - 1;
            int r = in.readInt() - 1;

            int lo = 0;
            int hi = n;
            while (lo < hi) {
                int mid = (lo + hi + 1) / 2;
                if (check(b, t, l, r, mid)) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }

            out.println(lo);
        }
    }

    IntegerSparseTable2D st;

    public boolean check(int b, int t, int l, int r, int mid) {
        if (mid == 0) {
            return true;
        }
        b += mid - 1;
        l += mid - 1;
        if (b > t || l > r) {
            return false;
        }
        return st.query(b, t, l, r) >= mid;
    }
}

