package on2020_05.on2020_05_23_AtCoder___AtCoder_Grand_Contest_044.A___Pay_to_Win;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.ArrayIndex;

import java.util.HashMap;
import java.util.Map;

public class APayToWin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();
        map.clear();

        long ans = dp(n);
        out.println(ans);
    }

    long a;
    long b;
    long c;
    long d;
    LongHashMap map = new LongHashMap(200, true);
    long inf = (long) 1e18;

    public long dp(long n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return d;
        }
        long ans = map.getOrDefault(n, -1);
        if (ans == -1) {

            ans = DigitUtils.mul(n, d, inf);

            //ceil 2
            ans = Math.min(ans, dp(n / 2) + n % 2 * d + a);
            ans = Math.min(ans, dp((n + 1) / 2) + (2 - n % 2) % 2 * d + a);
            ans = Math.min(ans, dp(n / 3) + n % 3 * d + b);
            ans = Math.min(ans, dp((n + 2) / 3) + (3 - n % 3) % 3 * d + b);
            ans = Math.min(ans, dp(n / 5) + n % 5 * d + c);
            ans = Math.min(ans, dp((n + 4) / 5) + (5 - n % 5) % 5 * d + c);

            map.put(n, ans);
        }

        return ans;
    }
}
