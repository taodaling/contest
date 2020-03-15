package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AKthTerm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        out.println(new int[]{1, 1, 1, 2, 1, 2, 1, 5, 2, 2, 1, 5, 1, 2, 1, 14, 1, 5, 1, 5, 2, 2, 1, 15, 2, 2, 5, 4, 1, 4, 1, 51}[k - 1]);
    }
}
