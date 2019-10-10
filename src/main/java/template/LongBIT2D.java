package template;

public class LongBIT2D {
    private long[][] data;
    private int n;
    private int m;

    /**
     * 创建大小A[1...n][1..,m]
     */
    public LongBIT2D(int n, int m) {
        this.n = n;
        this.m = m;
        data = new long[n + 1][m + 1];
    }

    /**
     * 查询左上角为(1,1)，右下角为(x,y)的矩形和
     */
    public long query(int x, int y) {
        long sum = 0;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                sum += data[i][j];
            }
        }
        return sum;
    }


    /**
     * 查询左上角为(ltx,lty)，右下角为(rbx,rby)的矩形和
     */
    public long rect(int ltx, int lty, int rbx, int rby) {
        return query(rbx, rby) - query(ltx - 1, rby) - query(rbx, lty - 1) + query(ltx - 1, lty - 1);
    }

    /**
     * 将A[x][y] 更新为A[x][y]+mod
     */
    public void update(int x, int y, long mod) {
        for (int i = x; i <= n; i += i & -i) {
            for (int j = y; j <= m; j += j & -j) {
                data[i][j] += mod;
            }
        }
    }

    /**
     * 将A全部清0
     */
    public void clear() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                data[i][j] = 0;
            }
        }
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
