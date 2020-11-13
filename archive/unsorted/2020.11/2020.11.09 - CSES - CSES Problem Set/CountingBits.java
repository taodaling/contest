package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class CountingBits {

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = 0;
        for (int i = 0; i < 60; i++) {
            long bit = 1L << i;
            long fullRound = n / (bit * 2);
            long contrib = fullRound * bit;
            long remain = n % (bit * 2);
            contrib += Math.max(0, remain - bit + 1);
            ans += contrib;
            if (contrib > 0) {
                debug.debug("i", i);
                debug.debug("contrib", contrib);
            }
        }
        out.println(ans);
    }
}
