package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.KahanSummation;
import template.utils.Debug;

public class DRandomizer {
    public long cross(int[] a, int[] b) {
        return (long)a[0] * b[1] - (long)a[1] * b[0];
    }

    public int intPointCount(int x, int y) {
        x = Math.abs(x);
        y = Math.abs(y);
        int g = GCDs.gcd(x, y);
        return g;
    }

    double[] floor;
    int n;
    double prob(int j){
        return (floor[j + 1] - floor[n]) / (1 - (1 + n + (double)n * (n - 1) / 2) * floor[n]);
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.ri();
            }
        }
        KahanSummation ea = new KahanSummation();
        KahanSummation eb = new KahanSummation();
        floor = new double[n + 1];
        floor[0] = 1;
        for(int i = 1; i <= n; i++){
            floor[i] = floor[i - 1] * 0.5;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n && j < 300; j++) {
                int to = j + i;
                if (to >= n) {
                    to -= n;
                }
                ea.add(prob(j) * cross(pts[i], pts[to]));
                eb.add(prob(j) * intPointCount(pts[to][0] - pts[i][0], pts[to][1] - pts[i][1]));
            }
        }
        debug.debug("ea", ea);
        debug.debug("eb", eb);
        double ans = ea.sum() / 2 - eb.sum() / 2 + 1;
        out.println(ans);
    }
}
