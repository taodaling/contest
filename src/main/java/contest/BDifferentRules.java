package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.NumberSumLeqMatch;

public class BDifferentRules {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        long maxMatch = 1 + new NumberSumLeqMatch(n, x + y).maxMatching(x, y);
        long minMatch = n - new NumberSumLeqMatch(n, 2 * (n + 1) - (x + y + 1))
                .maxMatching(n + 1 - x, n + 1 - y);

        out.println(minMatch);
        out.println(maxMatch);
    }
}
