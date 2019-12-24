package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ATemporarilyUnavailable {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int c = in.readInt();
        int radius = in.readInt();
        int l = c - radius;
        int r = c + radius;

        int ll = Math.max(l, a);
        int rr = Math.min(r, b);
        int cover = Math.max(0, rr - ll);
        out.println(b - a - cover);
    }
}
