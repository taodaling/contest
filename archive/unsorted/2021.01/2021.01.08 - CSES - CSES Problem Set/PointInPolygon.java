package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class PointInPolygon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        for (int i = 0; i < m; i++) {
            IntegerPoint2 pt = new IntegerPoint2(in.ri(), in.ri());
            int type = IntegerPoint2.inPolygon(Arrays.asList(pts), pt);
            out.println(type == 1 ? "INSIDE" : type == 2 ? "BOUNDARY" : "OUTSIDE");
        }
    }
}
