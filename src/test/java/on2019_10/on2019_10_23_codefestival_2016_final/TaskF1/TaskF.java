package on2019_10.on2019_10_23_codefestival_2016_final.TaskF1;



import template.FastInput;
import template.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i < n; i++) {
            a[i] = in.readInt();
        }
        int m = in.readInt();
        if (m != 1) {
            return;
        }
        a[n] = in.readInt();
        long[] pre = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            pre[i] = pre[i - 1] + a[i];
        }

        long[] dp = new long[n + 1];
        long mx = a[n] - pre[n - 1] - dp[n];
        for (int i = n - 1; i >= 2; i--) {
            dp[i] = pre[i] + mx;
            mx = Math.max(mx, a[i] - pre[i - 1] - dp[i]);
        }

        out.println(dp[2] + a[1] - a[2]);
    }
}
