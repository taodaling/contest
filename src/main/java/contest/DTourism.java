package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

import java.util.Random;

public class DTourism {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        long[][] dist = new long[k + 1][n];
        int[][] edges = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = in.readInt();
            }
        }
        int[] classify = new int[n];
        RandomWrapper rw = new RandomWrapper(new Random(0));
        long end = System.currentTimeMillis() + 2500;
        long ans = (long) 1e18;
        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < n; i++) {
                classify[i] = rw.nextInt(0, 1);
            }
            SequenceUtils.deepFill(dist, (long) 1e18);
            dist[0][0] = 0;
            classify[0] = 0;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < n; j++) {
                    if (classify[j] != i % 2) {
                        continue;
                    }
                    for (int t = 0; t < n; t++) {
                        if (j == t || classify[j] == classify[t]) {
                            continue;
                        }
                        dist[i + 1][t] = Math.min(dist[i + 1][t], dist[i][j] + edges[j][t]);
                    }
                }
            }
            ans = Math.min(ans, dist[k][0]);
        }

        out.println(ans);
    }
}


