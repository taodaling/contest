package on2020_07.on2020_07_09_TopCoder_SRM__747.RochesterSequence;



import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class RochesterSequence {
    //Debug debug = new Debug(false);

    public int[] solve(int[] Sprefix, int n, int a, int b, int m) {
        int[] S = new int[n];
        for (int i = 0; i <= Sprefix.length - 1; i++) {
            S[i] = Sprefix[i];
        }

        for (int i = Sprefix.length; i <= n - 1; i++) {
            S[i] = (int) (((long) S[i - 1] * a + b) % m);
        }


        Status[][] dp = new Status[n][n];
        List<int[]> intervals = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                intervals.add(new int[]{i, j, S[i] + S[j]});
            }
        }
        intervals.sort((x, y) -> x[2] == y[2] ? Integer.compare(x[1] - x[0], y[1] - y[0]) :
                -Integer.compare(x[2], y[2]));

        IntegerBIT2D bit = new IntegerBIT2D(n, n);
        for (int[] lr : intervals) {
            int l = lr[0];
            int r = lr[1];
            dp[l][r] = bit.query(mirror(l, n), r);
            dp[l][r].max++;
            bit.update(mirror(l, n) + 1, r + 1, dp[l][r]);
        }
        //debug.debug("dp", dp);

        Status ans = new Status();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                ans.update(dp[i][j]);
            }
        }

        return new int[]{ans.max * 2, ans.cnt};
    }

    public int mirror(int i, int n) {
        //n - 1 => 0
        return n - 1 - i;
    }
}

class Status {
    int max = 0;
    int cnt = 0;

    private static Modular mod = new Modular(1e9 + 7);

    public void update(Status s) {
        if (s.max > max) {
            max = s.max;
            cnt = 0;
        }
        if (s.max == max) {
            cnt = mod.plus(cnt, s.cnt);
        }
    }

    @Override
    public String toString() {
        return String.format("M:%d, C:%d", max, cnt);
    }
}

class IntegerBIT2D {
    private Status[][] data;
    private int n;
    private int m;

    /**
     * 创建大小A[1...n][1..,m]
     */
    public IntegerBIT2D(int n, int m) {
        this.n = n;
        this.m = m;
        data = new Status[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                data[i][j] = new Status();
            }
        }
    }

    /**
     * 查询左上角为(1,1)，右下角为(x,y)的矩形和
     */
    public Status query(int x, int y) {
        Status sum = new Status();
        sum.cnt = 1;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                sum.update(data[i][j]);
            }
        }
        return sum;
    }


    /**
     * 将A[x][y] 更新为A[x][y]+mod
     */
    public void update(int x, int y, Status mod) {
        for (int i = x; i <= n; i += i & -i) {
            for (int j = y; j <= m; j += j & -j) {
                data[i][j].update(mod);
            }
        }
    }
}
