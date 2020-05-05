package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BRaceAgainstTime {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int hh = in.readInt();
        int mm = in.readInt();
        int ss = in.readInt();
        int srcsrc = in.readInt();
        int dstdst = in.readInt();

        int h = parseHour(hh, mm, ss);
        int m = parseMinute(mm, ss);
        int s = parseSecond(ss);
        int src = parseHour(srcsrc, 0, 0);
        int dst = parseHour(dstdst, 0, 0);

        if (meet(h, m, s, src, dst, 1) ||
                meet(h, m, s, src, dst, -1)) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }

    public int mod = 12 * 60 * 60;

    public int parseHour(int h, int m, int s) {
        return (h * (mod / 12) + m * (mod / 12 / 60) + s * (mod / 12 / 60 / 60)) % mod;
    }

    public int parseMinute(int m, int s) {
        return m * (mod / 60) + s * (mod / 3600) % mod;
    }

    public int parseSecond(int s) {
        return s * (mod / 60) % mod;
    }

    public boolean meet(int h, int m, int s, int src, int dst, int step) {
        if (src == dst) {
            return true;
        }

        while (true) {
            src = DigitUtils.mod(src + step, mod);
            if (src == h || src == m || src == s) {
                return false;
            }
            if (src == dst) {
                return true;
            }
        }
    }
}
