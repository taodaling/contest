package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.LongComparator;
import template.utils.CompareUtils;
import template.utils.Debug;

public class EGoodsTransportation {
    long inf = (long) 1e18;

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long c = in.ri();
        int[] p = in.ri(n);
        int[] s = in.ri(n);
        int[] exceed = new int[n];
        int wpos = 0;
        for (int i = 0; i < n; i++) {
            long now = p[i] - s[i];
            int r = wpos - 1;
            long sum = 0;
            for (int j = 0; j < wpos; j++) {
                sum += exceed[j];
            }
            int lastJ = wpos - 1;
            boolean ok = false;
            for (int j = -1; j < wpos && !ok; j++) {
                long lastSub = j == -1 ? inf : exceed[j] - c;
                if (j >= 0) {
                    sum -= exceed[j];
                }
                if (r <= j) {
                    sum = 0;
                    r = j;
                }
                long cur = now + (j + 1) * c;
                while (r > j && exceed[r] <= cur) {
                    sum -= exceed[r];
                    r--;
                }
                boolean goon = false;
                while (r > j) {
                    long total = sum + cur;
                    long avg = DigitUtils.floorDiv(total, (r - j + 1));
                    long rm = total - avg * (r - j + 1);
                    long min = avg;
                    long max = min;
                    if (rm > 0) {
                        max++;
                    }
                    if (exceed[j + 1] - max > c) {
                        goon = true;
                        break;
                    }
                    if (min >= exceed[r] || min <= cur) {
                        sum -= exceed[r];
                        r--;
                        continue;
                    }
                    if (max > lastSub) {
                        lastJ = j - 1;
                        ok = true;
                    }
                    break;
                }
                if (goon) {
                    continue;
                }
                if (ok) {
                    break;
                }
                if (lastSub < cur) {
                    lastJ = j - 1;
                    ok = true;
                    break;
                }
            }
            sum = 0;
            for (int j = lastJ + 1; j < wpos; j++) {
                sum += exceed[j];
            }
            r = wpos - 1;
            long cur = now + (lastJ + 1) * c;
            while (r > lastJ) {
                long total = sum + cur;
                long avg = DigitUtils.floorDiv(total, (r - lastJ + 1));
                long rm = total - avg * (r - lastJ + 1);
                long min = avg;
                long max = min;
                if (rm > 0) {
                    max++;
                }
                if (min >= exceed[r] || min <= cur) {
                    sum -= exceed[r];
                    r--;
                    continue;
                }
                break;
            }
            long total = sum + cur;
            long avg = DigitUtils.floorDiv(total, (r - lastJ + 1));
            long rm = total - avg * (r - lastJ + 1);
            for (int j = 0; j <= lastJ; j++) {
                exceed[j] -= c;
            }
            for (int j = lastJ + 1; j <= r; j++) {
                exceed[j] = (int) avg;
                if (rm > 0) {
                    exceed[j]++;
                    rm--;
                }
            }
            exceed[wpos] = (int) avg;
            if (rm > 0) {
                exceed[wpos]++;
            }
            wpos++;
            int tail = wpos - 1;
            int end = exceed[tail];
            while (tail > 0 && exceed[tail - 1] < end) {
                exceed[tail] = exceed[tail - 1];
                tail--;
            }
            exceed[tail] = end;
            while (wpos > 0 && exceed[wpos - 1] <= 0) {
                wpos--;
            }
            debug.debug("exceed", exceed);
        }

        long sale = 0;
        for (int x : p) {
            sale += x;
        }
        for (int i = 0; i < wpos; i++) {
            sale -= exceed[i];
        }
        out.println(sale);
    }
}
