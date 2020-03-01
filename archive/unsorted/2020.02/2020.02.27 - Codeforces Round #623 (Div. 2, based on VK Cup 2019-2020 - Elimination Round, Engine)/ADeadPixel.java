package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ADeadPixel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        int area = Math.max(Math.max(x, a - x - 1) * b, Math.max(y, b - y - 1) * a);
        out.println(area);
    }
}
