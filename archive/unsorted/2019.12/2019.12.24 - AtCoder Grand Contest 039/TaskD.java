package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double xExp = 0;
        double yExp = 0;

        int n = in.readInt();
        double l = in.readInt();
        double ways = (double) n * (n - 1) * (n - 2) / 6;

        double[] ts = new double[n];
        for (int i = 0; i < n; i++) {
            ts[i] = in.readInt() / l * 2 * Math.PI ;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double half = (ts[i] + ts[j]) / 2D;
                double cos = Math.cos(half);
                double sin = Math.sin(half);
                double prob = ((n - 1 - j) - (j - 1 - i) + i) / ways;
                xExp += prob * cos;
                yExp += prob * sin;
            }
        }

        out.printf("%.15f %.15f", xExp, yExp);
    }
}
