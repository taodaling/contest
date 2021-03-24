package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

public class ADungeon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int c = in.ri();
        String yes = "yes";
        String no = "no";
        if ((a + b + c) % 9 != 0) {
            out.println(no);
            return;
        }
        int round = (a + b + c) / 9;
        int min = SortUtils.minOf(a, b, c);
        if (min < round) {
            out.println(no);
            return;
        }
        out.println(yes);
    }
}
