package on2020_06.on2020_06_03_TopCoder_SRM__752.ReconstructNumber;



import java.util.Arrays;

public class ReconstructNumber {
    public String smallest(String comparisons) {
        int n = comparisons.length() + 1;
        char[] cs = comparisons.toCharArray();
        boolean[][] dp = new boolean[n][10];
        int[][] last = new int[n][10];

        Arrays.fill(dp[n - 1], true);
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (match(j, k, cs[i]) && dp[i + 1][k]) {
                        dp[i][j] = true;
                        last[i][j] = k;
                        break;
                    }
                }
            }
        }

        int cur = -1;
        for (int i = 1; i < 10; i++) {
            if (dp[0][i]) {
                cur = i;
                break;
            }
        }

        if (cur == -1) {
            return "";
        }

        StringBuilder builder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            builder.append(cur);
            if (i == n - 1) {
                break;
            }
            cur = last[i][cur];
        }

        return builder.toString();
    }

    public boolean match(int l, int r, char c) {
        switch (c) {
            case '<':
                return l < r;
            case '>':
                return l > r;
            case '=':
                return l == r;
            case '!':
                return l != r;
        }
        return false;
    }
}
