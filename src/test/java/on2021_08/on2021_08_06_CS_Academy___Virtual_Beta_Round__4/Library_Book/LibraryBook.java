package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Library_Book;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

public class LibraryBook {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] a = new int[n][2];
        int[][] b = new int[m][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                a[i][j] = in.ri();
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                b[i][j] = in.ri();
            }
        }
        Arrays.sort(a, Comparator.comparingInt(x -> x[0]));
        Arrays.sort(b, Comparator.comparingInt(x -> x[0]));
        Deque<int[]> dq = new ArrayDeque<>(m + 1);
        for (int i = 0; i < m; i++) {
            while (!dq.isEmpty() && dq.peekLast()[1] <= b[i][1]) {
                dq.removeLast();
            }
            if (!dq.isEmpty() && dq.peekLast()[0] + dq.peekLast()[1] >= b[i][0] + b[i][1]) {
                continue;
            }
            dq.addLast(b[i]);
        }
        dq.addFirst(new int[]{0, 0});
        b = dq.toArray(new int[0][]);
        m = b.length - 1;
        debug.debug("b", b);
        int[] prev = new int[m + 1];
        int[] next = new int[m + 1];
        int inf = (int) 1e9;
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int[] e : a) {
            Arrays.fill(next, inf);
            for (int i = 0; i < m; i++) {
                next[i + 1] = Math.min(next[i + 1], Math.max(prev[i], b[i + 1][0]) + b[i + 1][1]);
            }
            for (int i = 1; i <= m; i++) {
                next[i] = Math.min(next[i], Math.max(next[i - 1], b[i][0] + b[i][1]));
            }
            for (int i = 0; i <= m; i++) {
                next[i] = Math.min(next[i], prev[i]);
                next[i] = Math.max(next[i], e[0]) + e[1];
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
            debug.debugArray("e", e);
            debug.debug("prev", prev);
        }
        int best = prev[m];
        for (int i = 0; i < m; i++) {
            int time = Math.max(prev[i] + b[i + 1][1], b[m - 1][0] + b[m - 1][1]);
            best = Math.min(best, time);
        }
        out.println(best);
    }
    Debug debug = new Debug(false);
}

