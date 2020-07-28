package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongMillerRabin;
import template.math.MillerRabin;

public class BTaxes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (LongMillerRabin.mr(n, 10)) {
            out.println(1);
            return;
        }
        if (n == 4) {
            out.println(2);
            return;
        }
        if (n % 2 == 0) {
            out.println(2);
        } else {
            if (LongMillerRabin.mr(n - 2, 10)) {
                out.println(2);
            } else {
                out.println(3);
            }
        }
    }
}
