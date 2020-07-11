package contest;

import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.utils.GeometryUtils;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

public class P2742USACO51FencingTheCows {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for(int i = 0; i < n; i++){
            pts[i] = new Point2(in.readDouble(), in.readDouble());
        }
        Point2[] convexHull = ConvexHull2.grahamScan(Arrays.asList(pts)).toArray(new Point2[0]);

        KahanSummation sum = new KahanSummation();
        for(int i = 0; i < convexHull.length; i++){
            Point2 cur = convexHull[i];
            Point2 next = convexHull[(i + 1) % convexHull.length];
            sum.add(Point2.dist(cur, next));
        }

        out.printf("%.2f", sum.sum()).println();
    }
}
