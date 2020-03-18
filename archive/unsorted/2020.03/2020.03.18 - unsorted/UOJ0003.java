package contest;

import template.graph.OfflineConnectionChecker;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.PriorityQueue;

public class UOJ0003 {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] edges = new int[m][4];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 4; j++) {
                edges[i][j] = in.readInt();
            }
        }

        int src = 1;
        int dst = n;

        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        OfflineConnectionChecker occ = new OfflineConnectionChecker(n + 1);
        PriorityQueue<Integer> pq = new PriorityQueue<>(m, (a, b) -> -Integer.compare(edges[a][3], edges[b][3]));
        debug.debug("edges", edges);
        int ans = (int) 1e8;
        int limit = 100000;
        for (int i = 0; i < m; i++) {
            int l = i;
            int r = i;
            while (r < m - 1 && edges[r + 1][2] == edges[l][2]) {
                r++;
            }
            i = r;
            for (int j = l; j <= r; j++) {
                occ.addEdge(edges[j][0], edges[j][1], time(edges[j][3] - 1));
                pq.add(j);
            }

            while (!pq.isEmpty()) {
                occ.elapse(time(edges[pq.peek()][3]));
                if (!occ.check(src, dst)) {
                    break;
                }
                int index = pq.remove();
                ans = Math.min(ans, edges[index][3] + edges[l][2]);
            }
        }

        if (ans == (int) 1e8) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }

    public int time(int x) {
        return 100000 - x;
    }
}
