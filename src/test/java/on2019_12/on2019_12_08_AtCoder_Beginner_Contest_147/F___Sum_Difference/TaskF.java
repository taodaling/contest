package on2019_12.on2019_12_08_AtCoder_Beginner_Contest_147.F___Sum_Difference;





import template.datastructure.IntervalBooleanMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Gcd;

import java.util.HashMap;
import java.util.Map;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int d = in.readInt();
        x -= d;

        if (d == 0) {
            if (x == 0) {
                out.println(1);
            } else {
                out.println(n + 1);
            }
            return;
        }

        int absX = Math.abs(x);
        int absD = Math.abs(d);
        int g = new Gcd().gcd(absX, absD);
        x /= g;
        d /= g;
        absD /= g;
        absX /= g;

        long inf = (long) 1e13;
        IntervalBooleanMap[] segs = new IntervalBooleanMap[n + 1];
        for (int i = 0; i <= n; i++) {
            segs[i] = new IntervalBooleanMap();
        }
        for (int i = 0; i <= n; i++) {
            int pick = DigitUtils.mod(i, absD);
            long differ = (long) (i - pick) * x / d;
            long min = sum(1, i) + differ;
            long max = sum(n - i + 1, n) + differ;
            segs[pick].setTrue(min, max);
        }

        long ans = 0;
        for (int i = 0; i <= n; i++) {
            if (segs[i] == null) {
                continue;
            }
            ans += segs[i].countTrue();
        }

        out.println(ans);
    }

    public long sum(long l, long r) {
        if (l > r) {
            return 0;
        }
        return (l + r) * (r - l + 1) / 2;
    }
}

