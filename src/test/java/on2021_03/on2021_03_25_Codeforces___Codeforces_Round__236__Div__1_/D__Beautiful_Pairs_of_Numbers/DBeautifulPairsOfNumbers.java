package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.D__Beautiful_Pairs_of_Numbers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.utils.SequenceUtils;

public class DBeautifulPairsOfNumbers {
    int mod = (int) 1e9 + 7;
    int maxK = 50;
    int maxN = 1000;
    long[][] dp;

    {
        long[][] prev = new long[maxN + 1][maxK + 1];
        long[][] next = new long[maxN + 1][maxK + 1];
        prev[0][0] = 1;
        for (int i = 0; i <= maxN; i++) {
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j <= maxN; j++) {
                for (int k = 0; k <= maxK; k++) {
                    next[j][k] += prev[j][k];
                    if (i + j <= maxN && k + 1 <= maxK) {
                        next[i + j][k + 1] += prev[j][k];
                    }
                }
            }
            for (int j = 0; j <= maxN; j++) {
                for (int k = 0; k <= maxK; k++) {
                    next[j][k] %= mod;
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        dp = prev;
    }

    Factorial fact = new Factorial((int) 1e5, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        if (k > maxK) {
            out.println(0);
            return;
        }
        long ans = 0;
        for (int i = 0; i <= n; i++) {
            if (dp[i][k] == 0) {
                continue;
            }
            //2 0
            //k - 1 >= 1
            int remain = (n - 1 - i) - (k - 1);
            //x1 + x2 + ... + x[k+1] = remain

            ans += dp[i][k] * comb.combination(remain + k, k) % mod;
        }

        ans %= mod;
        ans = ans * fact.fact(k) % mod;
        out.println(ans);
    }
}
