package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ARobotProgram {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int y = in.ri();
        x = Math.abs(x);
        y = Math.abs(y);
        if (x < y) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        int ans = y * 2;
        x -= y;
        if (x > 0) {
            ans += x + x - 1;
        }
        out.println(ans);
    }
}
