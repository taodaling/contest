package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BAAndB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();

        if (a == b) {
            out.println(0);
            return;
        }

        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        for (int i = 1; ; i++) {
            a += i;
            if (a >= b) {
                if ((a - b) % 2 == 0) {
                    out.println(i);
                } else if (i % 2 == 0) {
                    out.println(i + 1);
                } else {
                    out.println(i + 2);
                }
                return;
            }
        }
    }
}
