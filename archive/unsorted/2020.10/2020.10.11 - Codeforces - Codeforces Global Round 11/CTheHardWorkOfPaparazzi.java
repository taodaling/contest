package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class CTheHardWorkOfPaparazzi {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int r = in.readInt();
        int n = in.readInt();
        Task[] tasks = new Task[n + 1];
        tasks[0] = new Task(0, 1, 1);
        for (int i = 1; i <= n; i++) {
            tasks[i] = new Task(in.readInt(), in.readInt(), in.readInt());
        }

        int inf = (int) 1e9;
        int[] dp = new int[n + 1];
        int[] prefMax = new int[n + 1];
        Arrays.fill(dp, -inf);
        dp[0] = 0;
        prefMax[0] = dp[0];

        for (int i = 1; i <= n; i++) {
            for (int j = i - 1; j >= 0 && j >= i - 1000; j--) {
                if (possible(tasks[j], tasks[i])) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
            int last = i - 1000 - 1;
            if (last >= 0) {
                dp[i] = Math.max(dp[i], prefMax[last] + 1);
            }
            prefMax[i] = Math.max(prefMax[i - 1], dp[i]);
        }

        int ans = prefMax[n];
        out.println(ans);
    }

    public boolean possible(Task a, Task b) {
        int d = Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        int t = b.t - a.t;
        return d <= t;
    }
}

class Task {
    int t;
    int x;
    int y;

    public Task(int t, int x, int y) {
        this.t = t;
        this.x = x;
        this.y = y;
    }
}
