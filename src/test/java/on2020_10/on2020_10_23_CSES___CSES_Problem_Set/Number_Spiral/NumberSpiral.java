package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Number_Spiral;



import template.io.FastInput;

import java.io.PrintWriter;

public class NumberSpiral {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long y = in.readInt();
        long x = in.readInt();
        long level = Math.max(x, y);
        long innerLast = (level - 1) * (level - 1);
        if (level % 2 == 0) {
            //from top
            if (x == level) {
                innerLast += y;
            } else {
                innerLast += y + (level - x);
            }
        } else {
            if (y == level) {
                innerLast += x;
            } else {
                innerLast += x + (level - y);
            }
        }
        out.println(innerLast);
    }
}
