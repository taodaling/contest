package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ANickname {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.println(in.readString().substring(0, 3));
    }
}
