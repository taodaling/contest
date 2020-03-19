package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n == 1) {
            out.println(-1);
            return;
        }
        if ((n - 1) * 2 % 3 != 0) {
            for (int i = 0; i < n - 1; i++) {
                out.append(2);
            }
            out.println(3);
        } else {
            for (int i = 0; i < n - 2; i++) {
                out.append(2);
            }
            out.println(33);
        }
    }
}
