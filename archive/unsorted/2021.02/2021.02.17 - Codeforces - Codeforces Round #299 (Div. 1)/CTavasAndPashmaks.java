package contest;

import template.geometry.geo2.ConvexHull2;
import template.geometry.geo2.Point2;
import template.geometry.old.ConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;

public class CTavasAndPashmaks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2Ext[] pts = new Point2Ext[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2Ext(10000d / in.ri(), 10000d / in.ri());
        }
        Point2Ext[] convexHull = ConvexHull2.grahamScan(Arrays.asList(pts), true).toArray(new Point2Ext[0]);
        int leftest = SortUtils.argmin(i -> convexHull[i], 0, convexHull.length - 1,
                Comparator.<Point2Ext>comparingDouble(x -> x.x).thenComparingDouble(x -> x.y));
        int rightest = SortUtils.argmin(i -> convexHull[i], 0, convexHull.length - 1,
                Comparator.<Point2Ext>comparingDouble(x -> x.y).thenComparingDouble(x -> x.x));
        for (int i = leftest; ; i = (i + 1) % convexHull.length) {
            convexHull[i].ok = true;
            if(i == rightest){
                break;
            }
        }
        for(int i = 0; i < n; i++){
            if(pts[i].ok){
                out.append(i + 1).append(' ');
            }
        }
    }
}

class Point2Ext extends Point2 {
    boolean ok;

    public Point2Ext(double x, double y) {
        super(x, y);
    }
}