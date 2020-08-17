package on2020_08.on2020_08_17_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2020_Round_1.A2__Perimetric___Chapter_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.Debug;

import java.util.Map;
import java.util.TreeMap;

public class A2PerimetricChapter2 {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("testNumber", testNumber);
        sortByL.clear();
        horizontal = 0;
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int k = in.readInt();
        long[] L = new long[n + 1];
        long[] H = new long[n + 1];
        long[] W = new long[n + 1];

        for (int i = 1; i <= k; i++) {
            L[i] = in.readInt();
        }

        long al = in.readInt();
        long bl = in.readInt();
        long cl = in.readInt();
        long dl = in.readInt();


        for (int i = 1; i <= k; i++) {
            W[i] = in.readInt();
        }

        long aw = in.readInt();
        long bw = in.readInt();
        long cw = in.readInt();
        long dw = in.readInt();

        for (int i = 1; i <= k; i++) {
            H[i] = in.readInt();
        }

        long ah = in.readInt();
        long bh = in.readInt();
        long ch = in.readInt();
        long dh = in.readInt();

        for (int i = k + 1; i <= n; i++) {
            L[i] = (al * L[i - 2] + bl * L[i - 1] + cl) % dl + 1;
            H[i] = (ah * H[i - 2] + bh * H[i - 1] + ch) % dh + 1;
            W[i] = (aw * W[i - 2] + bw * W[i - 1] + cw) % dw + 1;
        }

        Modular mod = new Modular(1e9 + 7);
        int prod = 1;
        long vertical = 0;

        for (int i = 1; i <= n; i++) {
            int original = sortByL.size();
            add(L[i], L[i] + W[i]);
            int now = sortByL.size();
            vertical += (now - original) * H[i] * 2;
            long perimeter = vertical + horizontal * 2;
            perimeter = mod.valueOf(perimeter);

            prod = mod.mul(prod, perimeter);
        }

        out.println(prod);
    }

    TreeMap<Long, Interval> sortByL = new TreeMap<>();
    long horizontal = 0;

    public void add(long l, long r) {
        Interval interval = new Interval();
        interval.l = l;
        interval.r = r;

        while (!sortByL.isEmpty()) {
            Map.Entry<Long, Interval> floor = sortByL.floorEntry(interval.l);
            if (floor == null) {
                break;
            }
            Interval f = floor.getValue();
            if (f.r >= interval.l) {
                sortByL.remove(floor.getKey());
                horizontal -= f.r - f.l;
                interval.merge(f);
                continue;
            }
            break;
        }

        while (!sortByL.isEmpty()) {
            Map.Entry<Long, Interval> ceil = sortByL.ceilingEntry(interval.l);
            if (ceil == null) {
                break;
            }
            Interval c = ceil.getValue();
            if (c.l <= interval.r) {
                sortByL.remove(ceil.getKey());
                horizontal -= c.r - c.l;
                interval.merge(c);
                continue;
            }
            break;
        }
        sortByL.put(interval.l, interval);
        horizontal += interval.r - interval.l;
    }
}


class Interval {
    long l;
    long r;

    public void merge(Interval interval) {
        l = Math.min(interval.l, l);
        r = Math.max(interval.r, r);
    }
}