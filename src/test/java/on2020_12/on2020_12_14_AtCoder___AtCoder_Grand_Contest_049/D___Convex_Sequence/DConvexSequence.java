package on2020_12.on2020_12_14_AtCoder___AtCoder_Grand_Contest_049.D___Convex_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.primitve.generated.datastructure.IntegerArrayList;

public class DConvexSequence {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList left = new IntegerArrayList();
        IntegerArrayList right = new IntegerArrayList();
        left.add(n);
        for (int i = 0; i < n; i++) {
            long size = IntMath.sumOfInterval(1, i + 1);
            if (size <= m) {
                left.add((int) size);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            long size = IntMath.sumOfInterval(1, n - i);
            if (size <= m) {
                right.add((int) size);
            }
        }
        long[][] ldp = calc(left.toArray(), m);
        long[][] rdp = calc(right.toArray(), m);

        long ans = 0;
        for (int i = 0; i < n; i++) {
            long prefix = IntMath.sumOfInterval(1, i);
            if (prefix > m) {
                break;
            }
            int remain = (int) (m - prefix);
            long[] lchoice = ldp[i + 1];
            long[] rchoice = rdp[Math.min(rdp.length - 1, n - 1 - i)];
            for (int j = 0; j <= remain; j++) {
                ans += lchoice[j] * rchoice[remain - j] % mod;
            }
        }
        ans %= mod;
        out.println(ans);
    }

    public long[][] calc(int[] ws, int m) {
        int n = ws.length;
        long[][] dp = new long[n + 1][m + 1];
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            int w = ws[i];
            for (int j = 0; j < w; j++) {
                long sum = 0;
                for (int k = j; k <= m; k += w) {
                    sum += dp[i][k];
                    if (sum >= mod) {
                        sum -= mod;
                    }
                    dp[i + 1][k] = sum;
                }
            }
        }
        return dp;
    }
}
