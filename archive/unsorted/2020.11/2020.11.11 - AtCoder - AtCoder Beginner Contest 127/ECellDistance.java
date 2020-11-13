package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class ECellDistance {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[] x = new int[n * m];
        int[] y = new int[n * m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                x[i * n + j] = i;
                y[j * m + i] = j;
            }
        }
        long ans = (solve(x, k) + solve(y, k)) % mod;
        out.println(ans);
    }

    public long solve(int[] x, int k) {
        int n = x.length;
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            long contrib = (long) ((i - 1) - (n - i)) * x[i - 1] % mod;
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        ans = ans * comb.combination(n - 2, k - 2) % mod;
        return ans;
    }
}
