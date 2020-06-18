package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;


public class FLampsOnACircle {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = 0;
        int r = 0;
        for (int i = 1; i <= n; i++) {
            int block = DigitUtils.ceilDiv(n - 1, i + 1);
            int cnt = n - 1 - (block - 1) - i;
            if (block * (i + 1) == n - 1) {
                cnt--;
            }
            if (cnt > r) {
                r = cnt;
                k = i;
            }
        }

        debug.debug("r", r);
        debug.debug("k", k);

        if (r == 0) {
            out.println(0).flush();
            return;
        }

        BitSet end = new BitSet(n);
        for (int i = 0; i < n - 1; i++) {
            if (i % (k + 1) != k) {
                end.set(i);
            }
        }
        BitSet cur = new BitSet(n);
        for (; cur.size() < end.size() - k; ) {
            cur.xor(end);
            int len = cur.size();
            out.append(len).append(' ');
            for (int i = 0; i < n - 1; i++) {
                if (cur.get(i)) {
                    out.append(i + 1).append(' ');
                }
            }
            out.println().flush();

            int x = in.readInt() - 1;
            cur.copy(end);
            for (int i = 0; i < len; i++) {
                cur.clear((i + x) % n);
            }
        }

        out.println(0).flush();
    }
}
