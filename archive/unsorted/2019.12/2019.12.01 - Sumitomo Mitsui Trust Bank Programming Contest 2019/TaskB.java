package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i <= n; i++) {
            if (n - Math.floor(i * 1.08) == 0) {
                out.println(i);
                return;
            }
        }
        out.println(":(");
    }
}
