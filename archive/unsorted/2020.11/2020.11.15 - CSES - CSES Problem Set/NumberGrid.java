package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class NumberGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt() - 1;
        int y = in.readInt() - 1;
        out.println(x ^ y);
    }
}
