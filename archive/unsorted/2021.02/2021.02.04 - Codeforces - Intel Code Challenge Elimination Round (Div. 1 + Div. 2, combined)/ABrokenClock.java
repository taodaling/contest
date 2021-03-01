package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ABrokenClock {
    public int diff(int a, int b) {
        int ans = 0;
        if (a / 10 != b / 10) {
            ans++;
        }
        if (a % 10 != b % 10) {
            ans++;
        }
        return ans;
    }

    public String format(int x) {
        if (x < 10) {
            return "0" + x;
        }
        return "" + x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int format = in.ri();
        int h = in.ri();
        in.rc();
        int m = in.ri();
        int ans = (int) 1e9;
        int bestH = -1;
        int bestM = -1;
        for (int j = 0; j <= 59; j++) {
            if (format == 12) {
                for (int i = 1; i <= 12; i++) {
                    int cand = diff(h, i) + diff(j, m);
                    if (ans > cand) {
                        ans = cand;
                        bestH = i;
                        bestM = j;
                    }
                }
            } else {
                for (int i = 0; i <= 23; i++) {
                    int cand = diff(h, i) + diff(j, m);
                    if (ans > cand) {
                        ans = cand;
                        bestH = i;
                        bestM = j;
                    }
                }
            }
        }
        out.println(format(bestH) + ":" + format(bestM));

    }
}
