package template.primitve.generated.datastructure;

public class LongPreSum2D {
    long[][] ps;

    public LongPreSum2D(int n, int m) {
        ps = new long[n + 1][m + 1];
    }

    public LongPreSum2D(Int2ToLongFunction func, int n, int m) {
        ps = new long[n + 1][m + 1];
        init(func, n, m);
    }

    public void init(Int2ToLongFunction func, int n, int m) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                ps[i][j] = func.apply(i - 1, j - 1);
                ps[i][j] += ps[i][j - 1];
            }
            for (int j = 1; j <= m; j++) {
                ps[i][j] += ps[i - 1][j];
            }
        }
    }

    public long rect(int low, int hi, int l, int r) {
        low = Math.max(low, 0);
        l = Math.max(l, 0);
        hi = Math.min(hi, ps.length - 1);
        r = Math.min(r, ps[0].length - 1);
        if (l > r || low > hi) {
            return 0;
        }
        return ps[hi + 1][r + 1] - ps[low][r + 1] - ps[hi + 1][l] + ps[low][l];
    }

    public long leftTopCorder(int hi, int r) {
        if (hi < 0 || r < 0) {
            return 0;
        }
        return ps[hi + 1][r + 1];
    }
}
