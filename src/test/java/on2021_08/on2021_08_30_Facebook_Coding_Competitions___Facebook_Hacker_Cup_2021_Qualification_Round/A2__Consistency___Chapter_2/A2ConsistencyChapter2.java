package on2021_08.on2021_08_30_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2021_Qualification_Round.A2__Consistency___Chapter_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class A2ConsistencyChapter2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int q = in.ri();
        int inf = (int) 1e8;
        int charset = 'z' - 'a' + 1;
        int[][] dist = new int[charset][charset];
        SequenceUtils.deepFill(dist, inf);
        for (int i = 0; i < charset; i++) {
            dist[i][i] = 0;
        }
        for (int i = 0; i < q; i++) {
            int a = in.rc() - 'A';
            int b = in.rc() - 'A';
            dist[a][b] = Math.min(dist[a][b], 1);
        }
        for (int i = 0; i < charset; i++) {
            for (int j = 0; j < charset; j++) {
                for (int k = 0; k < charset; k++) {
                    dist[j][k] = Math.min(dist[j][k], dist[j][i] + dist[i][k]);
                }
            }
        }
        long[] cost = new long[charset];
        for (int i = 0; i < s.length; i++) {
            int v = s[i] - 'A';
            for (int j = 0; j < charset; j++) {
                cost[j] += dist[v][j];
            }
        }
        debug.debug("dist", dist);
        long min = Arrays.stream(cost).min().orElse(-1);
        long ans = min >= inf ? -1 : min;
        out.printf("Case #%d: %d", testNumber, ans).println();
    }
    Debug debug = new Debug(false);
}

