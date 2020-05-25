package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class BCuttingCarrot {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int h = in.readInt();

        double[] H = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            H[i] = h * Math.sqrt((double) (i + 1) / n);
        }
        debug.debug("H", H);
        for (int i = 0; i < n - 1; i++) {
            out.append(H[i]).append(' ');
        }
    }
}
