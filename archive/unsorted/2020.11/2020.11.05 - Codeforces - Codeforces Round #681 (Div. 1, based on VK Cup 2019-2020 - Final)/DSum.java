package contest;

import template.algo.UndoKnapsack;
import template.algo.UndoQueue;
import template.io.FastInput;
import template.io.FastOutput;

public class DSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[][] a = new int[n][];
        long[] sum = new long[n];
        for (int i = 0; i < n; i++) {
            int t = in.readInt();
            a[i] = new int[t];
            for (int j = 0; j < t; j++) {
                a[i][j] = in.readInt();
                sum[i] += a[i][j];
            }
        }

        UndoKnapsack knapsack = new UndoKnapsack(k);
        UndoQueue queue = new UndoQueue(n);
        for (int i = 1; i < n; i++) {
            queue.add(knapsack.add(a[i].length, sum[i]));
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                queue.add(knapsack.add(a[i - 1].length, sum[i - 1]));
                queue.remove();
            }

            long local = knapsack.dp[k];
            long ps = 0;
            for (int j = 0; j < a[i].length && j < k; j++) {
                ps += a[i][j];
                local = Math.max(local, ps + knapsack.dp[k - 1 - j]);
            }
            ans = Math.max(local, ans);
        }

        out.println(ans);
    }
}
