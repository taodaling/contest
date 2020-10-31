package contest;

import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;

public class CoinCombinationsII {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] coins = new int[n];
        in.populate(coins);
        int mod = (int) 1e9 + 7;
        int[] dp = new int[x + 1];
        dp[0] = 1;
        for (int c : coins) {
            for (int i = 0; i < c; i++) {
                int sum = 0;
                for (int j = i; j <= x; j += c) {
                    sum += dp[j];
                    if (sum >= mod) {
                        sum -= mod;
                    }
                    dp[j] = sum;
                }
            }
        }
        out.println(dp[x]);
    }
}
