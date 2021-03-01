package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ESubsequencesReturn {
    int log(int n, int k) {
        int ans = 0;
        while (n % k == 0) {
            n /= k;
            ans++;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int k = in.ri();
        int mod = (int) 1e9 + 7;
        int[] value = new int[3000];
        int[] dp = new int[3000];
        for (int i = 0; i < k; i++) {
            value[i] = i;
            long sum = 1;
            for(int j = 0; j < i; j++){
                sum += dp[j];
            }
            sum %= mod;
            dp[i] = (int) sum;
        }
        for (int i = k; i < 3000; i++) {
            value[i] = value[i - 1] + 1 + log(i, k);
            value[i] %= k;
            long sum = 0;
            for (int j = i - 1; ; j--) {
                sum += dp[j];
                if (value[j] == value[i]) {
                    break;
                }
            }
            sum %= mod;
            dp[i] = (int) sum;
        }

        for (int i = 1; i < 3000; i++) {
            dp[i] += dp[i - 1];
            if (dp[i] >= mod) {
                dp[i] -= mod;
            }
        }
        IntPoly poly = new IntPolyFFT(mod);
        ModLinearRecurrenceSolver solver =
                ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(new IntegerArrayList(dp),
                        mod, poly);
        long ans = solver.solve(n - 1, new IntegerArrayList(dp));
        ans = (ans + 1) % mod;
        out.println(ans);
    }
}
