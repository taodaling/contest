package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class StairGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sg = 0;
        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            if (i % 2 == 0) {
                sg ^= x;
            }
        }
        out.println(sg == 0 ? "second" : "first");
    }
}
