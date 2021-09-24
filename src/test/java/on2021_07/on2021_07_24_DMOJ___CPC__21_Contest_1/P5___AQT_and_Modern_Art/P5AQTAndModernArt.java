package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P5___AQT_and_Modern_Art;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

public class P5AQTAndModernArt {
    List<int[]> rects = new ArrayList<>(500);
    FastInput in;
    FastOutput out;

    int qtime = 0;
    public int query(int l, int r, int b, int t) {
        qtime++;
        assert qtime <= 61000;
        out.append("? ").append(l).append(' ').append(r).append(' ').append(b).append(' ').append(t).println()
                .flush();
        int ans = in.ri();
        if (ans == -1) {
            throw new RuntimeException();
        }
        for (int[] knownRect : rects) {
            if (knownRect[0] >= l && knownRect[1] <= r && knownRect[2] >= b && knownRect[3] <= t) {
                ans--;
            }
        }
        return ans;
    }

    public static int lastTrue(IntPredicate predicate, int l, int r) {
        assert l <= r;
        while (l != r) {
            int mid = DigitUtils.ceilAverage(l, r);
            if (predicate.test(mid)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }

    public static int firstTrue(IntPredicate predicate, int l, int r) {
        assert l <= r;
        while (l != r) {
            int mid = DigitUtils.floorAverage(l, r);
            if (predicate.test(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        int L = 1;
        int R = (int) 1e9;
        int B = 1;
        int T = (int) 1e9;
        while (rects.size() < n) {
            int last = qtime;
            int[] rect = new int[]{L, R, B, T};
            IntPredicate predicate = m -> {
                return query(m, rect[1], rect[2], rect[3]) > 0;
            };
            rect[0] = lastTrue(predicate, rect[0], rect[1]);
            assert qtime - last <= 30;
            predicate = m -> {
                return query(rect[0], m, rect[2], rect[3]) > 0;
            };
            rect[1] = firstTrue(predicate, rect[0], rect[1]);
            assert qtime - last <= 30 * 2;
            predicate = m -> {
                return query(rect[0], rect[1], m, rect[3]) > 0;
            };
            rect[2] = lastTrue(predicate, rect[2], rect[3]);
            assert qtime - last <= 30 * 3;
            predicate = m -> {
                return query(rect[0], rect[1], rect[2], m) > 0;
            };
            rect[3] = firstTrue(predicate, rect[2], rect[3]);
            assert qtime - last <= 30 * 4;
            int count = query(rect[0], rect[1], rect[2], rect[3]);
            assert qtime - last <= 121;
            for (int i = 0; i < count; i++) {
                rects.add(rect.clone());
            }
            debug.debug("used", qtime - last);
            assert qtime - last <= 121;
        }
        assert rects.size() == n;
        out.println("!");
        for (int[] rect : rects) {
            out.append(rect[0]).append(' ').append(rect[1]).append(' ').append(rect[2]).append(' ').append(rect[3]).println();
        }
        out.flush();
    }
}
