package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

public class PointLocationTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        IntegerPoint2[] pts = new IntegerPoint2[3];
        for (int i = 0; i < 3; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        int side = IntegerPoint2.orient(pts[0], pts[1], pts[2]);
        out.println(side > 0 ? "LEFT" : side == 0 ? "TOUCH" : "RIGHT");
    }

}
