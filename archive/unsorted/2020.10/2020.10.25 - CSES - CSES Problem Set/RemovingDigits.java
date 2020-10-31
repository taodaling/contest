package contest;

import template.io.FastInput;
import template.math.IntRadix;

import java.io.PrintWriter;

public class RemovingDigits {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        IntRadix radix = new IntRadix(10);
        int n = in.readInt();
        int[] dp = new int[n + 1];
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            int x = i;
            dp[i] = (int) 1e9;
            while (x != 0) {
                int tail = x % 10;
                x /= 10;
                if (tail != 0) {
                    dp[i] = Math.min(dp[i], dp[i - tail] + 1);
                }
            }
        }
        out.println(dp[n]);
    }
}
