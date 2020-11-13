package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class NimGameII {

    int sg(int n) {
        return n % 4;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sgSum = 0;
        for (int i = 0; i < n; i++) {
            sgSum ^= sg(in.readInt());
        }
        out.println(sgSum == 0 ? "second" : "first");
    }
}
