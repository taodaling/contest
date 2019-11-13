package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        int dist = Math.min(Math.abs(a - b) + x, n - 1);
        out.println(dist);
    }
}
