package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class ALikesDisplay {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        String suf = "";
        if (n >= 1000 && n < 1000_000) {
            suf = "K";
            n = (n + 500) / 1000;
            if (n >= 1000) {
                suf = "M";
                n = (n + 500) / 1000;
            }
        } else if (n >= 1000_000) {
            suf = "M";
            n = (n + 500000) / 1000_000;
        }
        out.append(n).println(suf);
    }
}
