package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

public class PolygonArea {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        long ans = IntegerPoint2.area2(pts);
        out.println(Math.abs(ans));
    }
}
