package on2021_05.on2021_05_30_AtCoder___AtCoder_Beginner_Contest_197_Sponsored_by_Panasonic_.F___Construct_a_Palindrome;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FConstructAPalindrome {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int charset = 'z' - 'a' + 1;
        List<Integer>[][] adj = new List[n][charset];
        boolean[][] edges = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < charset; j++) {
                adj[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.rc() - 'a';
            adj[a][c].add(b);
            adj[b][c].add(a);
            edges[a][b] = true;
            edges[b][a] = true;
        }
        int[][] dist = new int[n][n];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dist, inf);
        dist[0][n - 1] = 0;
        Deque<int[]> dq = new ArrayDeque<>(n * n);
        dq.add(new int[]{0, n - 1});
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            for (int i = 0; i < charset; i++) {
                for (int x : adj[head[0]][i]) {
                    for (int y : adj[head[1]][i]) {
                        if (dist[x][y] <= dist[head[0]][head[1]] + 2) {
                            continue;
                        }
                        dist[x][y] = dist[head[0]][head[1]] + 2;
                        dq.add(new int[]{x, y});
                    }
                }
            }
        }

        int best = inf;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    best = Math.min(best, dist[i][j]);
                    continue;
                }
                if(!edges[i][j]){
                    continue;
                }
                best = Math.min(best, dist[i][j] + 1);
            }
        }

        out.println(best == inf ? -1 : best);
    }

}


