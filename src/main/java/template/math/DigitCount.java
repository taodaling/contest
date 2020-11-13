package template.math;

import template.utils.SequenceUtils;

public class DigitCount {
    long[][][] way = new long[19][2][2];
    long[][][][] zero = new long[19][2][2][2];
    long top;
    long bot;
    LongRadix radix = new LongRadix(10);
    public long inf = (long) 2e18;
    int digit;

    long way(int d, int ceil, int floor) {
        if (d < 0) {
            return 1;
        }
        if (way[d][ceil][floor] == -1) {
            long ans = 0;
            int botD = radix.get(bot, d);
            int topD = radix.get(top, d);
            for (int i = 0; i < 10; i++) {
                if (i < botD && floor == 1 ||
                        i > topD && ceil == 1) {
                    continue;
                }
                ans += way(d - 1, ceil == 1 && i == topD ? 1 : 0,
                        floor == 1 && i == botD ? 1 : 0);
            }
            way[d][ceil][floor] = ans;
        }
        return way[d][ceil][floor];
    }

    long zero(int d, int ceil, int floor, int fill) {
        if (d < 0) {
            return 0;
        }
        if (zero[d][ceil][floor][fill] == -1) {
            long ans = 0;
            int botD = radix.get(bot, d);
            int topD = radix.get(top, d);
            for (int i = 0; i < 10; i++) {
                if (i < botD && floor == 1 ||
                        i > topD && ceil == 1) {
                    continue;
                }
                ans += zero(d - 1, ceil == 1 && i == topD ? 1 : 0,
                        floor == 1 && i == botD ? 1 : 0,
                        fill == 1 || i > 0 ? 1 : 0);
                if (i == digit && (digit > 0 || fill == 1)) {
                    ans += way(d - 1, ceil == 1 && i == topD ? 1 : 0,
                            floor == 1 && i == botD ? 1 : 0);
                }
                ans = Math.min(ans, inf);
            }
            zero[d][ceil][floor][fill] = ans;
        }
        return zero[d][ceil][floor][fill];
    }

    public long count(long l, long r, int digit) {
        top = r;
        bot = Math.max(l, 1);
        this.digit = digit;
        SequenceUtils.deepFill(way, -1L);
        SequenceUtils.deepFill(zero, -1L);
        long ans = zero(18, 1, 1, 0);
        if (l == 0 && digit == 0) {
            ans++;
        }
        return ans;
    }
}
