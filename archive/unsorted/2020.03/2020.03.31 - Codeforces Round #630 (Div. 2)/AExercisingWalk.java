package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AExercisingWalk {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int d = in.readInt();

        int x = in.readInt();
        int y = in.readInt();
        int x1 = in.readInt();
        int y1 = in.readInt();
        int x2 = in.readInt();
        int y2 = in.readInt();

        if (x - a + b < x1 || x - a + b > x2 || y - c + d < y1 || y - c + d > y2 || x1 == x2 && a + b > 0 ||
                y1 == y2 && c + d > 0) {
            out.println("No");
        } else {
            out.println("Yes");
        }
    }
}
