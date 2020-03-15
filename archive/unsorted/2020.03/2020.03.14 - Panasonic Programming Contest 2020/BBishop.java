package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BBishop {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long h = in.readInt();
        long w = in.readInt();
        long oddH = (h + 1) / 2;
        long oddW = (w + 1) / 2;
        long even = oddH * oddW +
                (h - oddH) * (w - oddW);
        if (h > 1 && w > 1) {
            out.println(even);
        } else {
            out.println(1);
        }
    }
}
