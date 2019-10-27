package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();

        int loop = 0;
        while (true) {
            n -= p;
            loop++;
            if (n < 0) {
                out.println(-1);
                return;
            }
            if (Integer.bitCount(n) <= loop && n >= loop) {
                out.println(loop);
                return;
            }
        }

    }
}
