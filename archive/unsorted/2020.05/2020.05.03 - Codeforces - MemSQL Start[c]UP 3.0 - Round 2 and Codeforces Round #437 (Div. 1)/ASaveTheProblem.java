package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASaveTheProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.readInt();

        out.append(Math.max(1, 2 * (A - 1))).append(' ').append(2).println();
        out.append(1).append(' ').append(2).println();
    }
}
