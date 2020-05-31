package on2020_05.on2020_05_29_AtCoder___AtCoder_Grand_Contest_020.B___Ice_Rink_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BIceRinkGame {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);
        SequenceUtils.reverse(a);

        long l = 2;
        long r = 2;
        for (long d : a) {
            l = DigitUtils.ceilDiv(l, d) * d;
            r = DigitUtils.minimumIntegerGreaterThanDiv(r, d) * d - 1;
            debug.debug("l", l);
            debug.debug("r", r);
            if (l > r) {
                out.println(-1);
                return;
            }
        }

        out.println(l);
        out.println(r);
    }
}
