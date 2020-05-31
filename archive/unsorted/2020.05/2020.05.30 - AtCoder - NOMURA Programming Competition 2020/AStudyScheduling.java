package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AStudyScheduling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h1 = in.readInt();
        int m1 = in.readInt();
        int h2 = in.readInt();
        int m2 = in.readInt();
        int k = in.readInt();

        int t1 = h1 * 60 + m1;
        int t2 = h2 * 60 + m2;

        int time = DigitUtils.mod(t2 - t1, 24 * 60);
        out.println(time - k);
    }
}
