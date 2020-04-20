package contest;

import template.math.Modular;

public class NiceMultiples {
    Modular mod = new Modular(1e9 + 7);


    public boolean check(long x, int b) {
        if (x == 0) {
            return true;
        }
        if (x % b == 0) {
            return false;
        }
        return check(x / b, b);
    }

    int[][][] dp;
    int[] up;
    int B;
    int m;

    public int dp(int i, int j, int ceil) {
        if (i < 0) {
            return j == 0 ? 1 : 0;
        }
        if (dp[i][j][ceil] == -1) {
            long ans = 0;
            for (int k = 1; k < B; k++) {
                if (ceil == 1 && k > up[i]) {
                    continue;
                }
                ans += dp(i - 1, (j * B + k) % m, ceil == 1 && k == up[i] ? 1 : 0);
            }
            dp[i][j][ceil] = mod.valueOf(ans);
        }
        return dp[i][j][ceil];
    }

    public int count(long M, long U, int B) {
        this.B = B;
        if (M >= 1e5) {
            int ans = 0;
            for (long i = M; i <= U; i += M) {
                if (check(i, B)) {
                    ans++;
                }
            }
            return mod.valueOf(ans);
        }

        this.m = (int) M;
        up = new int[50];
        for (int i = 0; i < 50; i++) {
            up[i] = (int) (U % B);
            U /= B;
        }
        int first = 50;
        while (first > 0 && up[first - 1] == 0) {
            first--;
        }

        dp = new int[50][m][2];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < first; i++) {
            ans += dp(i, 0, i == first - 1 ? 1 : 0);
        }
        return mod.valueOf(ans);
    }
}
