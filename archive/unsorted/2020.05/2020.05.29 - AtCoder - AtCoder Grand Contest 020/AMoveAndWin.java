package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AMoveAndWin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int between = Math.abs(a - b) - 1;
        if (between % 2 == 0) {
            out.println("Borys");
        } else {
            out.println("Alice");
        }
    }
}
