package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.CompareUtils;

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
        int min = CompareUtils.minOf(a, b, c);
        if (min < round) {
            out.println(no);
            return;
        }
        out.println(yes);
    }
}
