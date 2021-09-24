package on2021_08.on2021_08_20_CS_Academy___Round__11.Max_Intersection_Partition;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.*;

public class MaxIntersectionPartition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] lrs = new int[n][2];
        int[] longest = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                lrs[i][j] = in.ri();
            }
            longest[i] = lrs[i][1] - lrs[i][0];
        }
        Randomized.shuffle(longest);
        Arrays.sort(longest);

        Arrays.sort(lrs, Comparator.<int[]>comparingInt(x -> x[0]).thenComparingInt(x -> x[0] - x[1]));
        List<int[]> outside = new ArrayList<>(n);
        Deque<int[]> dq = new ArrayDeque<>(n);
        for (int[] lr : lrs) {
            while (!dq.isEmpty()) {
                int[] tail = dq.getLast();
                if (tail[1] >= lr[1]) {
                    outside.add(dq.removeLast());
                } else {
                    break;
                }
            }
            dq.addLast(lr);
        }
        List<int[]> inner = new ArrayList<>(dq);
        outside.sort(Comparator.comparingLong(x -> -(x[1] - x[0])));
        inner.sort(Comparator.comparingLong(x -> x[0]));
        int[][] data = inner.toArray(new int[0][]);
        long[] ps = new long[n];
        for (int i = 0; i < outside.size(); i++) {
            int[] e = outside.get(i);
            ps[i] = e[1] - e[0];
            if (i > 0) {
                ps[i] += ps[i - 1];
            }
        }
        for (int i = outside.size(); i < n; i++) {
            if (i > 0) {
                ps[i] = ps[i - 1];
            }
        }
        if (data.length == 0) {
            out.println(ps[k - 1]);
            return;
        }
        long inf = (long) 1e18;
        long[] prev = new long[data.length];
        long[] next = new long[data.length];
        Arrays.fill(prev, -inf);
        long best = 0;
        for(int i = 0; i < k - 1; i++){
            best += longest[n - 1 - i];
        }
        for (int i = 0; i < k; i++) {
            best = Math.max(prev[data.length - 1] + ps[k - 1 - i], best);
            long maxDp = 0;
            long maxDelta = data[0][1];
            for (int j = 0; j < data.length; j++) {
                next[j] = Math.max(maxDp, maxDelta - data[j][0]);
                maxDp = Math.max(maxDp, prev[j]);
                if (j + 1 < data.length) {
                    maxDelta = Math.max(maxDelta, prev[j] + data[j + 1][1]);
                }
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        best = Math.max(prev[data.length - 1], best);
        out.println(best);
    }
}
