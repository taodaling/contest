package on2020_12.on2020_12_25_Codeforces___Codeforces_Round__326__Div__1_.B__Duff_in_Beach;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BDuffInBeach {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long l = in.rl();
        int k = in.ri();
        int[] a = new int[n];
        in.populate(a);
        int[] indices = IntStream.range(0, n).toArray();
        CompareUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]),
                0, n);
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            inv[indices[i]] = i;
        }

        long block = l / n % mod;
        Randomized.shuffle(a);
        Arrays.sort(a);
        long[][] A;
        {
            long[] prev = new long[a.length];
            prev[a.length - 1] = 1;
            A = dp(a, k, prev);
        }
        long[][] B;
        {
            long[] prev = new long[a.length];
            for (int j = 0; j < l % n; j++) {
                prev[inv[j]] = 1;
            }
            B = dp(a, k - 1, prev);
        }
        debug.debugMatrix("A", A);
        debug.debugMatrix("B", B);
        long ans1 = 0;
        for (int i = 1; i <= k; i++) {
            if ( l / n < i) {
                continue;
            }
            long sum = 0;
            for (long x : A[i]) {
                sum += x;
            }
            sum %= mod;
            ans1 += sum * (block + 1 - i) % mod;
        }
        debug.debug("ans1", ans1);
        long ans2 = 0;
        for (int i = 0; i < k; i++) {
            if ( l / n < i) {
                continue;
            }
            long sum = 0;
            for (long x : B[i]) {
                sum += x;
            }
            sum %= mod;
            ans2 += sum;
        }

        debug.debug("ans2", ans2);
        long ans = DigitUtils.mod(ans1 + ans2, mod);
        out.println(ans);
    }

    long[][] dp(int[] a, int round, long[] prev) {
        long[][] ans = new long[round + 1][a.length];
        ans[0] = prev;
        for (int i = 1; i <= round; i++) {
            long sum = 0;
            for (int j = a.length - 1, k = a.length; j >= 0; j--) {
                while (k - 1 >= 0 && a[k - 1] >= a[j]) {
                    k--;
                    sum += ans[i - 1][k];
                    if (sum >= mod) {
                        sum -= mod;
                    }
                }
                ans[i][j] = sum;
            }
        }
        return ans;
    }
}
