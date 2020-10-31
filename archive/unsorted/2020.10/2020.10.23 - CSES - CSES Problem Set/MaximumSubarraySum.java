package contest;

import template.io.FastInput;

import java.io.PrintWriter;

public class MaximumSubarraySum {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long last = 0;
        long ans = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long now = last + in.readInt();
            ans = Math.max(ans, now);
            last = Math.max(now, 0);
        }
        out.println(ans);
    }
}
