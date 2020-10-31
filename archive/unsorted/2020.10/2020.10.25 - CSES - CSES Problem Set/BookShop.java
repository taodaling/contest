package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class BookShop {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] h = new int[n];
        int[] s = new int[n];
        in.populate(h);
        in.populate(s);
        int[] dp = new int[x + 1];
        for (int i = 0; i < n; i++) {
            for (int j = x; j - h[i] >= 0; j--) {
                dp[j] = Math.max(dp[j], dp[j - h[i]] + s[i]);
            }
        }
        int ans = Arrays.stream(dp).max().orElse(-1);
        out.println(ans);
    }
}
