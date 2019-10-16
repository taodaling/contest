package contest;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int t = in.readInt();
        int max = 0;
        for (int i = 0; i < t; i++) {
            max = Math.max(max, in.readInt());
        }

        if (max <= (k + 1) / 2) {
            out.println(0);
            return;
        }

        out.println(max - (k - max) - 1);
    }
}
