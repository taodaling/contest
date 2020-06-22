import java.util.Arrays;

public class AqaAsadiTrains {
    int[] T;
    int k;
    int[] A;
    int[] C;
    int[][][] dp;
    int impossible = (int) 1e9;
    int inf = (int) 1e8;

    public int dp(int l, int r, int t) {
        if (l > r) {
            return -inf;
        }
        if (dp[l][r][t] == impossible) {
            if (l == r) {
                if (t == k) {
                    return dp[l][r][t] = Math.max(0, C[A[l]]);
                }
                return dp[l][r][t] = A[l] == t ? 0 : -inf;
            }

            dp[l][r][t] = 0;
            for (int i = 0; i <= k; i++) {
                for (int j = 0; j <= k; j++) {
                    if (t(i, j) != t) {
                        continue;
                    }
                    for (int x = l; x < r; x++) {
                        dp[l][r][t] = Math.max(dp[l][r][t], dp(l, x, i) + dp(x + 1, r, j));
                        dp[l][r][t] = Math.max(dp[l][r][t], dp(l, x, j) + dp(x + 1, r, i));
                    }
                }
            }

            if (t == k) {
                //we may remove the result
                for (int i = 0; i < k; i++) {
                    dp[l][r][t] = Math.max(dp[l][r][t], dp(l, r, i) + C[i]);
                }
            }
        }
        return dp[l][r][t];
    }

    public int getMax(int[] C, int[] A, int[] T) {
        this.C = C;
        this.A = A;
        k = C.length;
        this.T = T;
        int n = A.length;
        dp = new int[n][n][k + 1];
        SequenceUtils.deepFill(dp, impossible);
        int ans = dp(0, n - 1, k);
        return ans;
    }

    public int t(int i, int j) {
        if (i == k || j == k) {
            return i == k ? j : i;
        }
        return T[i * k + j];
    }

}

class SequenceUtils {
    public static void deepFill(Object array, int val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            Arrays.fill(intArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

}
