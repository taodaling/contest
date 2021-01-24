package contest;

import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.IntegerConvexHull2;
import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Collection;

public class ConvexHull {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        Collection<IntegerPoint2> ch = IntegerConvexHull2.grahamScan(Arrays.asList(pts), true);

        out.println(ch.size());
        for (IntegerPoint2 pt : ch) {
            out.append(pt.x).append(' ').append(pt.y).println();
        }
    }
}
