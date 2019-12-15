package on2019_12.on2019_12_14_Educational_Codeforces_Round_56__Rated_for_Div__2_.F__Vasya_and_Array0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class FVasyaAndArray {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int len = in.readInt();
        int[][] dp = new int[n][k + 1];
        int[] sum = new int[n];

        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readInt();
        }

        if (len == 1) {
            out.println(0);
            return;
        }

        if (s[0] == -1) {
            for (int i = 1; i <= k; i++) {
                dp[0][i] = 1;
            }
        } else {
            dp[0][s[0]] = 1;
        }

        int[] lastDifference = new int[n];
        for (int i = 0; i < n; i++) {
            if (s[i] == -1) {
                if (i == 0) {
                    lastDifference[i] = -1;
                } else if (s[i - 1] == -1) {
                    lastDifference[i] = lastDifference[i - 1];
                } else {
                    lastDifference[i] = i - 1;
                }
                continue;
            }
            int j = i - 1;
            while (j >= 0 && s[j] == -1) {
                j--;
            }
            if (j >= 0) {
                lastDifference[i] = s[i] == s[j] ? lastDifference[j] : j;
            } else {
                lastDifference[i] = -1;
            }
        }

        sum[0] = sumOf(dp[0], 1, k);
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                if (s[i] == -1 || s[i] == j) {
                    dp[i][j] = sum[i - 1];
                    if (i >= len - 1 && (i - lastDifference[i] >= len || s[lastDifference[i]] == j &&
                            i - lastDifference[lastDifference[i]] >= len)) {
                        if (i == len - 1) {
                            dp[i][j] = mod.subtract(dp[i][j], 1);
                        } else {
                            dp[i][j] = mod.subtract(dp[i][j], mod.subtract(sum[i - len], dp[i - len][j]));
                        }
                    }
                }
            }
            sum[i] = sumOf(dp[i], 1, k);
        }

        int ans = sumOf(dp[n - 1], 1, k);
        out.println(ans);
    }

    public int sumOf(int[] x, int l, int r) {
        long ans = 0;
        for (int i = l; i <= r; i++) {
            ans += x[i];
        }
        return mod.valueOf(ans);
    }
}
