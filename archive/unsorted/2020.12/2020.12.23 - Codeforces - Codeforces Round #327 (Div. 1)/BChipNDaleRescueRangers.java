package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.DoublePredicate;

public class BChipNDaleRescueRangers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x1 = in.ri();
        int y1 = in.ri();
        int x2 = in.ri();
        int y2 = in.ri();
        double dx = x2 - x1;
        double dy = y2 - y1;
        int vmax = in.ri();
        int t = in.ri();
        int vx = -in.ri();
        int vy = -in.ri();
        int wx = -in.ri();
        int wy = -in.ri();

        DoublePredicate predicate = mid -> {
            double tx = dx;
            double ty = dy;
            tx += Math.min(mid, t) * vx;
            ty += Math.min(mid, t) * vy;
            tx += Math.max(mid - t, 0) * wx;
            ty += Math.max(mid - t, 0) * wy;
            return tx * tx + ty * ty <= vmax * vmax * mid * mid;
        };
        assert predicate.test(10);
        double ans = BinarySearch.firstTrue(predicate, 0, 1e20, 1e-10, 1e-10);
        out.println(ans);
    }
}
