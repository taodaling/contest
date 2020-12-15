package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class KidsAndPrizes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        double ans = n * (1 - Math.pow((double) (n - 1) / n, m));
        out.println(ans);
    }
}
