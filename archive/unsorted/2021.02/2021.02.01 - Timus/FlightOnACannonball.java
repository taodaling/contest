package contest;


import template.algo.DoubleBinarySearch;
import template.geometry.geo2.Circle2;
import template.geometry.geo2.Point2;
import template.geometry.old.GeoConstant;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.CompareUtils;

import java.util.*;

public class FlightOnACannonball {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(in.ri(), in.ri());
        }
        double ans = KthNearestLineToPoint.query(Point2.ORIGIN, pts, 1, 1e-8, 1e-8);
        out.println(ans);
    }
}

class KthNearestLineToPoint {
    /**
     * <pre>
     * 给定n个顶点，两两形成n(n-1)/2条直线，求这些直线中离center最近的第k条线离center的距离
     * </pre>
     * O(n\log_2n\log_2M)
     */
    public static double query(Point2 center, Point2[] points, long k, double relativeError, double absoluteError) {
        int n = points.length;
        Item[] items = new Item[n];
        double farthest = 1e50;
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].pt = points[i];
            farthest = Math.min(farthest, Point2.dist2(items[i].pt, center));
        }
        farthest = Math.sqrt(farthest);


        Item[] outter = new Item[n];
        Deque<Item> dq = new ArrayDeque<>(n);
        DoubleBinarySearch dbs = new DoubleBinarySearch(relativeError, absoluteError) {
            @Override
            public boolean check(double mid) {
                Circle2 c = new Circle2(center, mid);
                List<Point2> a2 = new ArrayList<>(2);
                List<Point2> b2 = new ArrayList<>(2);
                int wpos = 0;
                dq.clear();
                double maxL = 0;
                for (Item p : items) {
                    a2.clear();
                    b2.clear();
                    if (c.contain(p.pt)) {
                        return true;
                    }
                    outter[wpos++] = p;
                    Circle2.tangent(c.center, c.r, p.pt, 0, false, a2, b2);
                    p.t1 = Math.round(GeoConstant.theta(a2.get(0).x, a2.get(0).y) * 1e18);
                    p.t2 = Math.round(GeoConstant.theta(a2.get(1).x, a2.get(1).y) * 1e18);
                    if (p.t1 > p.t2) {
                        long tmp = p.t1;
                        p.t1 = p.t2;
                        p.t2 = tmp;
                    }
                    maxL = Math.max(maxL, p.t1);
                }
                if(wpos == 0){
                    return false;
                }
                CompareUtils.radixSortLongObject(outter, 0, wpos - 1, x -> x.t1);
                Item last = null;
                for(int i = 0; i < wpos; i++){
                    Item pt = outter[i];
                    if(last != null && pt.t2 < last.t2){
                        return true;
                    }
                    last = pt;
                }
                Item a = outter[0];
                Item b = outter[wpos - 1];
                if(a.t2 < b.t2){
                    return false;
                }
                return true;
            }
        };
        //dbs.check(0.8);
        return dbs.binarySearch(0, farthest);
    }

    public static class Item {
        public Point2 pt;
        long t1;
        long t2;
    }
}
