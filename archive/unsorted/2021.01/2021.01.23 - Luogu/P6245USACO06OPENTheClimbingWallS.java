package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P6245USACO06OPENTheClimbingWallS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int n = in.ri();
        int[][] pts = new int[n][];
        for (int i = 0; i < n; i++) {
            pts[i] = in.ri(2);
        }
        int inf = (int) 1e8;
        int[] dists = new int[n];
        Arrays.fill(dists, inf);
        int limit = 1000;
        int limit2 = limit * limit;
        for (int i = 0; i < n; i++) {
            if (pts[i][1] <= limit) {
                dists[i] = 1;
            }
        }
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            int index = -1;
            for (int j = 0; j < n; j++) {
                if (visited[j]) {
                    continue;
                }
                if (index == -1 || dists[j] < dists[index]) {
                    index = j;
                }
            }
            visited[index] = true;
            for (int k = 0; k < n; k++) {
                if (visited[k]) {
                    continue;
                }
                int dx = pts[index][0] - pts[k][0];
                int dy = pts[index][1] - pts[k][1];
                if (dx * dx + dy * dy <= limit2) {
                    dists[k] = Math.min(dists[k], dists[index] + 1);
                }
            }
        }
        int ans = inf;
        for (int i = 0; i < n; i++) {
            if (pts[i][1] + limit >= h) {
                ans = Math.min(ans, dists[i]);
            }
        }
        out.println(ans);
    }
}
