package on2021_05.on2021_05_24_Codeforces___Codeforces_Round__722__Div__1_.D__It_s_a_bird__No__it_s_a_plane__No__it_s_AaParsa_;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;

public class DItsABirdNoItsAPlaneNoItsAaParsa {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][n];
        SequenceUtils.deepFill(mat, -1);
        for (int i = 0; i < m; i++) {
            int a = in.ri();
            int b = in.ri();
            int c = in.ri();
            mat[a][b] = c;
        }

        int[][] dists = new int[n][n];
        int inf = (int) (1e9 + 1e6);
        int[] dist = new int[n];
        int[] dirty = new int[n];
        boolean[] handled = new boolean[n];
        for (int u = 0; u < n; u++) {
            Arrays.fill(dist, inf);
            Arrays.fill(dirty, inf);
            dist[u] = 0;
            Arrays.fill(handled, false);
            for (int round = 0; round < n; round++) {
                int head = -1;
                for (int i = 0; i < n; i++) {
                    if (!handled[i] && (head == -1 || dist[head] > dist[i])) {
                        head = i;
                    }
                }
                handled[head] = true;
                for (int i = 0, offset = dist[head] % n; i < n; i++, offset = offset + 1 == n ? 0 : offset + 1) {
                    if (mat[head][i] == -1) {
                        continue;
                    }
                    //start at
                    dirty[offset] = Math.min(dirty[offset], dist[head] + mat[head][i] - offset);
                    dirty[0] = Math.min(dirty[0], dist[head] + n - offset + mat[head][i]);
                }

                for (int i = 0; i < n; i++) {
                    if (i > 0) {
                        dirty[i] = Math.min(dirty[i], dirty[i - 1]);
                    }
                    dist[i] = Math.min(dist[i], dirty[i] + i);
                }
            }

            dists[u] = dist.clone();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(dists[i][j]).append(' ');
            }
            out.println();
        }
    }
}

