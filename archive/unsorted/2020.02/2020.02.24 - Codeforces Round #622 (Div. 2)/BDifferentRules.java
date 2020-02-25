package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BDifferentRules {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        if (x > y) {
            int tmp = x;
            x = y;
            y = tmp;
        }

        //larger
        int first = Math.min(x + y - 1, n);
        int max = first;
        if (x <= first || y <= first) {
            max--;
        }
        max++;


        x = n - x + 1;
        y = n - y + 1;
        int second = Math.min(x + y - 1 - 1, n);
        int min = second;
        if (x <= min || y <= min) {
            min--;
        }

        out.println(n - min);
        out.println(max);
    }
}
