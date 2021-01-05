package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class FMaxCorrectSet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int y = in.ri();
        if (x > y) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        int ans = 0;
        for (int i = 0; i < 1 << y; i++) {
            boolean valid = true;
            for (int j = 0; j < y; j++) {
                if (Bits.get(i, j) == 1) {
                    valid = valid && !(j >= x && Bits.get(i, j - x) == 1);
                }
            }
            if (!valid) {
                continue;
            }
            long built = i;
            for (int j = y; j < x + y; j++) {
                if (Bits.get(i, j - y) + Bits.get(i, j - x) > 0) {
                    continue;
                }
                built = Bits.set(built, j);
            }
            int round = n / (x + y);
            int cand = round * Long.bitCount(built) +
                    Long.bitCount(built & ((1L << n % (x + y)) - 1));
            ans = Math.max(ans, cand);
        }
        out.println(ans);
    }
}
