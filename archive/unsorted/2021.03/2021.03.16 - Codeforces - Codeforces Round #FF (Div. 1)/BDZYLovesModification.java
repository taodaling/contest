package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BDZYLovesModification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        long p = in.ri() * 2;
        long[][] mat = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri() * 2;
            }
        }

        long[] row1 = new long[n];
        long[] col1 = new long[m];
        long[] row2 = new long[n];
        long[] col2 = new long[m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                row1[i] += mat[i][j] + p / 2;
                col1[j] += mat[i][j] + p / 2;
                row2[i] -= p / 2;
                col2[j] -= p / 2;
            }
        }
        long ricj = p;

        long[] rowProfit = bestProfit(row1, row2, k);
        long[] colProfit = bestProfit(col1, col2, k);
        long ans = Long.MIN_VALUE;
        for (int i = 0; i <= k; i++) {
            long cand = rowProfit[i] + colProfit[k - i] - ricj * i * (k - i);
            ans = Math.max(ans, cand);
        }

        assert ans % 2 == 0;
        ans /= 2;
        out.println(ans);
    }

    public long[] bestProfit(long[] x1, long[] x2, int k) {
        int n = x1.length;
        Choice[] choices = new Choice[n];
        PriorityQueue<Choice> pq = new PriorityQueue<>(k, Comparator.<Choice>comparingLong(x -> x.delta).reversed());
        for (int i = 0; i < n; i++) {
            choices[i] = new Choice(x1[i], x2[i], 0);
            choices[i].calc();
            pq.add(choices[i]);
        }
        long[] profit = new long[k + 1];
        for (int i = 1; i <= k; i++) {
            Choice head = pq.remove();
            profit[i] = profit[i - 1] + head.delta;
            head.now++;
            head.calc();
            pq.add(head);
        }
        return profit;
    }
}

class Choice {
    //x2 * c^2 + x1 * c^1
    long x1;
    long x2;
    long now;

    public Choice(long x1, long x2, long now) {
        this.x1 = x1;
        this.x2 = x2;
        this.now = now;
    }

    long delta;

    public long eval(long c) {
        return (x2 * c + x1) * c;
    }

    public void calc() {
        delta = eval(now + 1) - eval(now);
    }
}