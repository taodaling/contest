package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m1 = in.readInt();
        int d1 = in.readInt();
        int m2 = in.readInt();
        int d2 = in.readInt();
        out.println(m1 == m2 ? 0 : 1);
    }
}
