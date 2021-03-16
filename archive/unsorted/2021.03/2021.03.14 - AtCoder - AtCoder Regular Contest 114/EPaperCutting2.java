package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class EPaperCutting2 {
    int mod = 998244353;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        int w = in.ri();
        int x1 = in.ri() - 1;
        int y1 = in.ri() - 1;
        int x2 = in.ri() - 1;
        int y2 = in.ri() - 1;

        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y1 > y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        long ans = 1;
        int fix = (x2 - x1) + (y2 - y1);
        for (int i = 0; i < h - 1; i++) {
            if (i >= x1 && i < x2) {
                continue;
            }
            int chance;
            if (i < x1) {
                chance = x1 - i + fix;
            } else {
                chance = i - x2 + 1 + fix;
            }
            ans += pow.inverse(chance);
        }
        for (int i = 0; i < w - 1; i++) {
            if (i >= y1 && i < y2) {
                continue;
            }
            int chance;
            if (i < y1) {
                chance = y1 - i + fix;
            } else {
                chance = i - y2 + 1 + fix;
            }
            ans += pow.inverse(chance);
        }
        ans %= mod;
        out.println(ans);
    }
}
