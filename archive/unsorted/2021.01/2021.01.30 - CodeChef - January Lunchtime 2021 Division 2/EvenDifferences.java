package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EvenDifferences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            cnts[in.ri() & 1]++;
        }
        int ans = Math.min(cnts[0], cnts[1]);
        out.println(ans);
    }
}
