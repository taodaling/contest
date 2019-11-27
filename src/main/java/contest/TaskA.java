package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int c = in.readInt();
            int s = in.readInt();
            int avg = s / c;
            long local = (long) (avg + 1) * (avg + 1) * (s % c);
            local += (long) avg * avg * (c - s % c);
            out.println(local);
        }
    }
}
