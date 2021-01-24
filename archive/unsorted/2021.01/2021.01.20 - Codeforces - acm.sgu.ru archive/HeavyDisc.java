package contest;

import template.geometry.geo2.Circle2;
import template.geometry.geo2.Point2;
import template.geometry.old.GeoConstant;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.SimpsonIntegral;

import java.util.ArrayList;
import java.util.List;

public class HeavyDisc {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x0 = in.ri();
        int y0 = in.ri();
        int r0 = in.ri();
        double eps = 1e-15;
        double c = Math.sqrt(x0 * x0 + y0 * y0);
        SimpsonIntegral si = new SimpsonIntegral(eps, r -> {
            double a = r;
            double b = r0;
            double angle = GeoConstant.triangleAngle(b, a, c);
            if (Double.isNaN(angle)) {
                return 0;
            }
            double arc = angle * 2 * a;
            double ans = arc * Math.log(r * r);
            return ans;
        });
        double ans = si.integral(c - r0, c + r0);
        out.println(ans);
    }
}
