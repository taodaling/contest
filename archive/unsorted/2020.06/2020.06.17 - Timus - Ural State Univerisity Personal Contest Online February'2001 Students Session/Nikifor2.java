package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Nikifor2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        for (int i = 2; i <= x; i++) {
            if(test(x, y, i)){
                out.println(i);
                return;
            }
        }
        out.println("No solution");
    }

    public boolean test(int x, int y, int radix) {
        if (y == 0) {
            return true;
        }
        if (x == 0) {
            return false;
        }
        if (y % radix == x % radix) {
            return test(x / radix, y / radix, radix);
        }
        return test(x / radix, y, radix);
    }
}
