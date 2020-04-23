package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.lang.management.LockInfo;
import java.util.Arrays;
import java.util.BitSet;

public class CNastyaAndUnexpectedGuest {
    //Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] d = new int[m];
        for (int i = 0; i < m; i++) {
            d[i] = in.readInt();
        }
        Randomized.shuffle(d);
        Arrays.sort(d);

        int g = in.readInt();
        int r = in.readInt();

        boolean[][] dp = new boolean[m][g + 1];
        dp[0][g] = true;
        IntegerDeque pend = new IntegerDequeImpl(m);
        LongDeque dq = new LongDequeImpl(m * (g + 1));
        dq.addLast(merge(0, g));
        int round = 0;
        boolean valid = false;

        while (!dq.isEmpty()) {
            round++;
            while (!dq.isEmpty()) {
                long head = dq.removeFirst();
                int high = (int) (head >> 32);
                int low = (int) head;
//                debug.debug("high", high);
//                debug.debug("low", low);
                valid = valid || high == m - 1;

                if (low == 0) {
                    pend.addLast(high);
                    continue;
                }

                //left
                if (high > 0) {
                    int leftTime = low - (d[high] - d[high - 1]);
                    if (leftTime >= 0 && !dp[high - 1][leftTime]) {
                        dp[high - 1][leftTime] = true;
                        dq.addLast(merge(high - 1, leftTime));
                    }
                }

                if (high < m - 1) {
                    int rightTime = low - (d[high + 1] - d[high]);
                    if (rightTime >= 0 && !dp[high + 1][rightTime]) {
                        dp[high + 1][rightTime] = true;
                        dq.addLast(merge(high + 1, rightTime));
                    }
                }
            }

            if (valid) {
                int time = -1;
                for (int i = 0; i <= g; i++) {
                    if (dp[m - 1][i]) {
                        time = i;
                    }
                }

                int cost = (round - 1) * (g + r) + g - time;
                out.println(cost);
                return;
            }

            while (!pend.isEmpty()) {
                int high = pend.removeFirst();
                int low = g;

                if (high > 0) {
                    int leftTime = low - (d[high] - d[high - 1]);
                    if (leftTime >= 0 && !dp[high - 1][leftTime]) {
                        dp[high - 1][leftTime] = true;
                        dq.addLast(merge(high - 1, leftTime));
                    }
                }

                if (high < m - 1) {
                    int rightTime = low - (d[high + 1] - d[high]);
                    if (rightTime >= 0 && !dp[high + 1][rightTime]) {
                        dp[high + 1][rightTime] = true;
                        dq.addLast(merge(high + 1, rightTime));
                    }
                }
            }
        }

        out.println(-1);
    }

    public long merge(long a, long b) {
        return (a << 32) | b;
    }
}
