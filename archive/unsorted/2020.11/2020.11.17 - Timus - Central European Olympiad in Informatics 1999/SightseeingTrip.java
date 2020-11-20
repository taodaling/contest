package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class SightseeingTrip {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if(n == -1){
            throw new UnknownError();
        }
        int m = in.readInt();
        int inf = (int) 1e6;
        int[][] edge = new int[n][n];
        SequenceUtils.deepFill(edge, inf);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edge[a][b] = edge[b][a] = Math.min(edge[a][b], in.readInt());
        }

        boolean[] visited = new boolean[n];
        int[] dists = new int[n];
        int[] prev = new int[n];

        IntegerArrayList list = new IntegerArrayList(n);
        int best = inf;
        for (int i = 0; i < n; i++) {
            Arrays.fill(dists, inf);
            Arrays.fill(visited, false);
            Arrays.fill(prev, -1);
            dists[i] = 0;
            for (int time = 0; time < n; time++) {
                int index = -1;
                for (int j = 0; j < n; j++) {
                    if (!visited[j] && (index == -1 || dists[index] > dists[j])) {
                        index = j;
                    }
                }
                visited[index] = true;
                for (int j = 0; j < n; j++) {
                    if (visited[j]) {
                        continue;
                    }
                    if (dists[index] + edge[index][j] < dists[j]) {
                        dists[j] = dists[index] + edge[index][j];
                        prev[j] = index;
                    }
                }
            }

            int localBest = inf;
            int end1 = -1;
            int end2 = -1;
            for (int a = 0; a < n; a++) {
                for (int b = 0; b < a; b++) {
                    //tree edge
                    if (prev[a] == b || prev[b] == a) {
                        continue;
                    }
                    if (localBest > dists[a] + dists[b] + edge[a][b]) {
                        localBest = dists[a] + dists[b] + edge[a][b];
                        end1 = a;
                        end2 = b;
                    }
                }
            }
            if (localBest < best) {
                best = localBest;
                list.clear();
                for (int cur = end1; cur != -1; cur = prev[cur]) {
                    list.add(cur);
                }
                list.pop();
                int now = list.size();
                for (int cur = end2; cur != -1; cur = prev[cur]) {
                    list.add(cur);
                }
                list.reverse(now, list.size() - 1);
            }
        }

        if (best == inf) {
            out.println("No solution.");
            return;
        }

        debug.debug("best", best);
        for (int x : list.toArray()) {
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}
