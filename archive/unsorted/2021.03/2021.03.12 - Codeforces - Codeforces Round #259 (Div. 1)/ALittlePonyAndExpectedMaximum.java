package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ALittlePonyAndExpectedMaximum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();

        double exp = 0;
        for (int i = 1; i <= m; i++) {
            double prob = Math.pow((double) i / m, n) - Math.pow((double) (i - 1) / m, n);
            exp += prob * i;
        }
        out.println(exp);
    }
}
