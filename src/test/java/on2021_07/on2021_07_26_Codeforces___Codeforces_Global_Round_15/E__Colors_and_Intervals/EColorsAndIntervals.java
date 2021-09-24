package on2021_07.on2021_07_26_Codeforces___Codeforces_Global_Round_15.E__Colors_and_Intervals;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.Comparator;

public class EColorsAndIntervals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] c = in.ri(n * k);
        for (int i = 0; i < c.length; i++) {
            c[i]--;
        }
        int[][] set = new int[n][];
        int[][] pts = new int[n][k + 1];
        int[] wpos = new int[n];
        for (int i = 0; i < n; i++) {
            pts[i][k] = i;
        }
        for (int i = 0; i < c.length; i++) {
            int v = c[i];
            pts[v][wpos[v]++] = i;
        }
        int max = DigitUtils.ceilDiv(n, k - 1);
        for (int i = 1; i < k; i++) {
            int finalI = i;
            Arrays.sort(pts, Comparator.comparingInt(x -> x[finalI]));
            int get = 0;
            for (int j = 0; j < pts.length && get < max; j++) {
                if (set[pts[j][k]] != null) {
                    continue;
                }
                get++;
                set[pts[j][k]] = new int[]{pts[j][i - 1], pts[j][i]};
            }
        }
        for(int i = 0; i < n; i++){
            out.append(set[i][0] + 1).append(' ').append(set[i][1] + 1).println();
        }
    }
}
