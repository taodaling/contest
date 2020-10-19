package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class EKeepGraphDisconnected {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        DSUExt dsu = new DSUExt(n);
        dsu.reset();
        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            dsu.merge(u, v);
        }

        int odd = 0;
        int cur = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                int size = dsu.size[dsu.find(i)];
                if (size % 2 == 1) {
                    odd++;
                }
                cur ^= choose2(size) & 1;
            }
        }

        cur ^= m & 1;
        int a = dsu.size[dsu.find(0)] % 2;
        int b = dsu.size[dsu.find(n - 1)] % 2;
        if (a == 1) {
            odd--;
        }
        if (b == 1) {
            odd--;
        }
        dp = new int[2][2][2][n];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(a, b, cur, odd);
        out.println(ans == 1 ? "First" : "Second");
    }

    public int dp(int a, int b, int cur, int n) {
        if (dp[a][b][cur][n] == -1) {
            boolean next = false;
            if (cur > 0) {
                next = next || dp(a, b, 0, n) == 0;
            }
            if (n > 0) {
                next = next || dp(a ^ 1, b, cur ^ a ^ 1, n - 1) == 0 ||
                        dp(a, b ^ 1, cur ^ b ^ 1, n - 1) == 0;
            }
            if (n >= 2) {
                next = next || dp(a, b, cur ^ 1 ^ 1, n - 2) == 0;
            }
            dp[a][b][cur][n] = next ? 1 : 0;
        }
        return dp[a][b][cur][n];
    }

    int[][][][] dp;

    public long choose2(long n) {
        return n * (n - 1) / 2;
    }
}


class DSUExt extends DSU {
    int[] size;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    @Override
    public void reset() {
        super.reset();
        Arrays.fill(size, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        size[a] += size[b];
    }
}