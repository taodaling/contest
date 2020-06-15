package on2020_06.on2020_06_15_Codeforces___Codeforces_Round__409__rated__Div__1__based_on_VK_Cup_2017_Round_2_.B__Volatile_Kite;



import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BVolatileKite {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.readInt(), in.readInt());
        }
        double d = 1e50;
        for (int i = 0; i < n; i++) {
            Point2 prev = pts[DigitUtils.mod(i - 1, n)];
            Point2 next = pts[DigitUtils.mod(i + 1, n)];
            Line2 line = new Line2(prev, next);
            double dist = line.dist(pts[i]);
            d = Math.min(d, dist / 2);
        }

        out.println(d);
    }
}
