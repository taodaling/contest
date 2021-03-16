package template.primitve.generated.datastructure;

public class IntegerBIT2DExt {
    int[][] delta;
    int[][] idelta;
    int[][] jdelta;
    int[][] ijdelta;
    int n;
    int m;

    public IntegerBIT2DExt(int n, int m) {
        this.n = n;
        this.m = m;
        delta = new int[n + 1][m + 1];
        idelta = new int[n + 1][m + 1];
        jdelta = new int[n + 1][m + 1];
        ijdelta = new int[n + 1][m + 1];
    }

    public void clear(int n, int m) {
        this.n = n;
        this.m = m;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                delta[i][j] = idelta[i][j] = jdelta[i][j] = ijdelta[i][j] = 0;
            }
        }
    }

    public void clear(Int2ToIntegerFunction func, int n, int m) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                delta[i][j] = func.apply(i, j);
            }
        }
        for (int i = n; i > 0; i--) {
            for (int j = n; j > 0; j--) {
                delta[i][j] = delta[i][j] - delta[i - 1][j]
                        - delta[i][j - 1] + delta[i - 1][j - 1];
                idelta[i][j] = i * delta[i][j];
                jdelta[i][j] = j * delta[i][j];
                ijdelta[i][j] = j * idelta[i][j];
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int ni = i + (i & -i);
                int nj = j + (j & -j);
                if (ni <= n) {
                    delta[ni][j] += delta[i][j];
                    idelta[ni][j] += idelta[i][j];
                    jdelta[ni][j] += jdelta[i][j];
                    ijdelta[ni][j] += ijdelta[i][j];
                }
                if (nj <= m) {
                    delta[i][nj] += delta[i][j];
                    idelta[i][nj] += idelta[i][j];
                    jdelta[i][nj] += jdelta[i][j];
                    ijdelta[i][nj] += ijdelta[i][j];
                }
                if (ni <= n && nj <= m) {
                    delta[ni][nj] -= delta[i][j];
                    idelta[ni][nj] -= idelta[i][j];
                    jdelta[ni][nj] -= jdelta[i][j];
                    ijdelta[ni][nj] -= ijdelta[i][j];
                }
            }
        }
    }

    public void clear() {
        clear(n, m);
    }

    private void update(int x, int y, int mod) {
        int x1 = mod;
        int x2 = mod * x;
        int x3 = mod * y;
        int x4 = mod * x * y;
        for (int i = x; i <= n; i += i & -i) {
            for (int j = y; j <= m; j += j & -j) {
                delta[i][j] += x1;
                idelta[i][j] += x2;
                jdelta[i][j] += x3;
                ijdelta[i][j] += x4;
            }
        }
    }

    public void update(int ltx, int lty, int rbx, int rby, int mod) {
        update(ltx, lty, mod);
        update(rbx + 1, lty, -mod);
        update(ltx, rby + 1, -mod);
        update(rbx + 1, rby + 1, mod);
    }

    public int query(int x, int y) {
        int ans1 = 0;
        int ans2 = 0;
        int ans3 = 0;
        int ans4 = 0;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                ans1 += delta[i][j];
                ans2 += idelta[i][j];
                ans3 += jdelta[i][j];
                ans4 += ijdelta[i][j];
            }
        }
        return ans1 * (x + 1) * (y + 1) - ans2 * (y + 1) - ans3 * (x + 1) + ans4;
    }

    public int query(int ltx, int lty, int rbx, int rby) {
        int ans = query(rbx, rby);
        if (ltx > 1) {
            ans -= query(ltx - 1, rby);
        }
        if (lty > 1) {
            ans -= query(rbx, lty - 1);
        }
        if (ltx > 1 && lty > 1) {
            ans += query(ltx - 1, lty - 1);
        }
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                builder.append(query(i, j) + query(i - 1, j - 1) - query(i - 1, j) - query(i, j - 1)).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
