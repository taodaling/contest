package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Magic {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long c = in.rl();

        long x = in.rl();
        long y = in.rl();
        long z = in.rl();

        long ax = Math.min(a, x);
        long by = Math.min(b, y);
        a -= ax;
        x -= ax;
        b -= by;
        y -= by;

        long cx = Math.min(c, x);
        c -= cx;
        x -= cx;

        long cy = Math.min(c, y);
        c -= cy;
        y -= cy;
        z -= a + b + c;

        if(x <= 0 && y <= 0 && z <= 0) {
            out.println("It is a kind of magic");
            return;
        }
        out.println("There are no miracles in life");
    }
}
