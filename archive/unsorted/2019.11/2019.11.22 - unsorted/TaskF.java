package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.ArrayIndex;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        long[] presumOfA = new long[n];
        for (int i = 1; i < n; i++) {
            a[i] = in.readInt();
            presumOfA[i] = presumOfA[i - 1] + a[i];
        }

        ArrayIndex aiNM = new ArrayIndex(n + 1, m + 1);
        int[] b = new int[aiNM.totalSize()];
        int[] l = new int[aiNM.totalSize()];
        int[] r = new int[aiNM.totalSize()];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                b[aiNM.indexOf(i, j)] = in.readInt();
            }
        }

        ArrayIndex aiNN = new ArrayIndex(n + 1, n + 1);
        long[] intervals = new long[aiNN.totalSize()];
        IntDeque deque = new IntDeque(n);
        for (int i = 1; i <= m; i++) {
            deque.clear();
            for (int j = 1; j <= n; j++) {
                while (!deque.isEmpty() && b[aiNM.indexOf(deque.peekLast(), i)] <= b[aiNM.indexOf(j, i)]) {
                    deque.removeLast();
                }
                if (deque.isEmpty()) {
                    l[aiNM.indexOf(j, i)] = 1;
                } else {
                    l[aiNM.indexOf(j, i)] = deque.peekLast() + 1;
                }
                deque.addLast(j);
            }
            deque.clear();
            for (int j = n; j >= 1; j--) {
                while (!deque.isEmpty() && b[aiNM.indexOf(deque.peekFirst(), i)] < b[aiNM.indexOf(j, i)]) {
                    deque.removeFirst();
                }
                if (deque.isEmpty()) {
                    r[aiNM.indexOf(j, i)] = n;
                } else {
                    r[aiNM.indexOf(j, i)] = deque.peekFirst() - 1;
                }
                deque.addFirst(j);
            }
        }


        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int x = l[aiNM.indexOf(i, j)];
                int y = r[aiNM.indexOf(i, j)];
                intervals[aiNN.indexOf(i, y)] += b[aiNM.indexOf(i, j)];
                intervals[aiNN.indexOf(x - 1, y)] -= b[aiNM.indexOf(i, j)];
                intervals[aiNN.indexOf(i, i - 1)] -= b[aiNM.indexOf(i, j)];
                intervals[aiNN.indexOf(x - 1, i - 1)] += b[aiNM.indexOf(i, j)];
            }
        }

        // push down
        for (int i = n + n; i >= 0; i--) {
            for (int j = 1; j <= n; j++) {
                int k = i - j;
                if (k < 1 || k > n) {
                    continue;
                }
                if (j + 1 <= n) {
                    intervals[aiNN.indexOf(j, k)] += intervals[aiNN.indexOf(j + 1, k)];
                }
                if (k + 1 <= n) {
                    intervals[aiNN.indexOf(j, k)] += intervals[aiNN.indexOf(j, k + 1)];
                }
                if (j + 1 <= n && k + 1 <= n) {
                    intervals[aiNN.indexOf(j, k)] -= intervals[aiNN.indexOf(j + 1, k + 1)];
                }
            }
        }

        long ans = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                long profit = intervals[aiNN.indexOf(i, j)] - (presumOfA[j - 1] - presumOfA[i - 1]);
                ans = Math.max(ans, profit);
            }
        }

        out.println(ans);
    }
}
