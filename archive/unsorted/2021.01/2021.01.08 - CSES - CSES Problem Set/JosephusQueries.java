package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.JosephusCircle;

public class JosephusQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int ans = JosephusCircle.dieAtRound(n, 2, k);
        out.println(ans + 1);
    }
}
