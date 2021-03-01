package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MultiMatch;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.ri();
        int[] pattern;
        pattern = in.ri(p);
        int m = in.ri();
        int s = in.ri();
        int[][] source = new int[m][s];
        for (int i = 0; i < m; i++) {
            source[i] = in.ri(s);
        }
        boolean[] ans = new MultiMatch().match(source, pattern, true);
        for (int i = 0; i < p; i++) {
            out.append(ans[i] ? 1 : 0);
        }
    }
}
