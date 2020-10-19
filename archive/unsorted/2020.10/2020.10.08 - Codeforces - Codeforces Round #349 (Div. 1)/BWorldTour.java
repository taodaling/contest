package contest;

import template.io.FastInput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.*;

public class BWorldTour {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        List<Integer>[] adj = new List[n];
        int[][] pqFrom = new int[n][n];
        int[][] pqTo = new int[n][n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            adj[u].add(v);
        }
        int[][] dists = new int[n][n];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dists, inf);
        Deque<Integer> dq = new ArrayDeque<>(n);

        for (int i = 0; i < n; i++) {
            int[] localD = dists[i];
            dists[i][i] = 0;
            dq.addLast(i);
            while (!dq.isEmpty()) {
                int head = dq.removeFirst();
                for (int node : adj[head]) {
                    int cand = dists[i][head] + 1;
                    if (localD[node] > cand) {
                        localD[node] = cand;
                        dq.addLast(node);
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            int[] localD = dists[i];
            PriorityQueue<Integer> farthest = new PriorityQueue<>((a, b) -> Integer.compare(localD[a], localD[b]));
            for (int j = 0; j < n; j++) {
                if (localD[j] == inf) {
                    continue;
                }
                farthest.add(j);
                if (farthest.size() > 4) {
                    farthest.remove();
                }
            }
            pqFrom[i] = farthest.stream().mapToInt(Integer::intValue).toArray();
        }

        for (int i = 0; i < n; i++) {
            int finalI = i;
            PriorityQueue<Integer> farthest = new PriorityQueue<>((a, b) -> Integer.compare(dists[a][finalI], dists[b][finalI]));
            for (int j = 0; j < n; j++) {
                if (dists[j][i] == inf) {
                    continue;
                }
                farthest.add(j);
                if (farthest.size() > 4) {
                    farthest.remove();
                }
            }
            pqTo[i] = farthest.stream().mapToInt(Integer::intValue).toArray();
        }

        int[] cur = new int[4];
        int max = -1;
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                if (a == b || dists[a][b] == inf) {
                    continue;
                }
                for (int x : pqTo[a]) {
                    if (a == x || x == b) {
                        continue;
                    }
                    for (int y : pqFrom[b]) {
                        if (a == y || b == y || x == y) {
                            continue;
                        }
                        int cand = dists[x][a] + dists[a][b] + dists[b][y];
                        if (cand > max) {
                            max = cand;
                            cur[0] = x;
                            cur[1] = a;
                            cur[2] = b;
                            cur[3] = y;
                        }
                    }
                }
            }
        }

        for(int x : cur){
            out.print(x + 1);
            out.append(' ');
        }

        debug.debug("max", max);
    }

    Debug debug = new Debug(true);
}


