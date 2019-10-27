package contest;

import template.FastInput;
import template.FastOutput;
import template.IntDeque;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        long[] presumOfA = new long[n];
        for (int i = 1; i < n; i++) {
            a[i] = in.readInt();
            presumOfA[i] = presumOfA[i - 1] + a[i];
        }
        int[][] b = new int[n + 1][m + 1];
        int[][] l = new int[n + 1][m + 1];
        int[][] r = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                b[i][j] = in.readInt();
            }
        }

        long[][] intervals = new long[n + 1][n + 1];
        IntDeque deque = new IntDeque(n);
        for (int i = 1; i <= m; i++) {
            deque.clear();
            for (int j = 1; j <= n; j++) {
                while (!deque.isEmpty() && b[deque.peekLast()][i] <= b[j][i]) {
                    deque.removeLast();
                }
                if (deque.isEmpty()) {
                    l[j][i] = 1;
                } else {
                    l[j][i] = deque.peekLast() + 1;
                }
                deque.addLast(j);
            }
            deque.clear();
            for (int j = n; j >= 1; j--) {
                while (!deque.isEmpty() && b[deque.peekFirst()][i] < b[j][i]) {
                    deque.removeFirst();
                }
                if (deque.isEmpty()) {
                    r[j][i] = n;
                } else {
                    r[j][i] = deque.peekFirst() - 1;
                }
                deque.addFirst(j);
            }
        }


        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int x = l[i][j];
                int y = r[i][j];
                intervals[i][y] += b[i][j];
                intervals[x - 1][y] -= b[i][j];
                intervals[i][i - 1] -= b[i][j];
                intervals[x - 1][i - 1] += b[i][j];
            }
        }

        //push down
        for (int i = n + n; i >= 0; i--) {
            for (int j = 1; j <= n; j++) {
                int k = i - j;
                if (k < 1 || k > n) {
                    continue;
                }
                if (j + 1 <= n) {
                    intervals[j][k] += intervals[j + 1][k];
                }
                if (k + 1 <= n) {
                    intervals[j][k] += intervals[j][k + 1];
                }
                if (j + 1 <= n && k + 1 <= n) {
                    intervals[j][k] -= intervals[j + 1][k + 1];
                }
            }
        }

        long ans = 0;
        for(int i = 1; i <= n; i++){
            for(int j = i; j <= n; j++){
                long profit = intervals[i][j] - (presumOfA[j - 1] - presumOfA[i - 1]);
                ans = Math.max(ans, profit);
            }
        }

        out.println(ans);
    }
}
