package contest;

import template.algo.DoubleBinarySearch;
import template.geometry.GeoConstant;
import template.geometry.geo2.Circle2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleArrayList;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.ArrayList;
import java.util.List;

public class FLineDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        Item[] pts = new Item[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Item();
            pts[i].a = new Point2(in.readInt(), in.readInt());
        }

        IntegerBIT bit = new IntegerBIT(2 * n);
        DoubleArrayList dal = new DoubleArrayList(2 * n);
        List<Item> outter = new ArrayList<>(n);
        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-7, 1e-7) {
            @Override
            public boolean check(double mid) {
                Circle2 c = new Circle2(Point2.ORIGIN, mid);
                List<Point2> a2 = new ArrayList<>(2);
                List<Point2> b2 = new ArrayList<>(2);
                outter.clear();
                dal.clear();
                bit.clear();
                for (Item p : pts) {
                    a2.clear();
                    b2.clear();
                    if (c.contain(p.a)) {
                        continue;
                    }
                    outter.add(p);
                    Circle2.tangent(c.center, c.r, p.a, 0, false, a2, b2);
                    p.t1 = GeoConstant.theta(a2.get(0).x, a2.get(0).y);
                    p.t2 = GeoConstant.theta(a2.get(1).x, a2.get(1).y);
                    dal.add(p.t1);
                    dal.add(p.t2);
                }
                dal.unique();
                for (Item pt : outter) {
                    pt.l = dal.binarySearch(pt.t1) + 1;
                    pt.r = dal.binarySearch(pt.t2) + 1;
                    if (pt.l > pt.r) {
                        int tmp = pt.l;
                        pt.l = pt.r;
                        pt.r = tmp;
                    }
                }
                outter.sort((a, b) -> Integer.compare(a.r, b.r));
                long noIntersect = 0;
                for (Item pt : outter) {
                    noIntersect += bit.query(pt.l, pt.r);
                    bit.update(pt.r, 1);
                    bit.update(pt.l - 1, -1);
                }
                return (long) n * (n - 1) / 2 - noIntersect >= k;
            }
        };

        dbs.check(3.8);
        double r = dbs.binarySearch(0, 1e6);
        out.println(r);
    }
}

class Item {
    Point2 a;
    double t1;
    double t2;
    int l;
    int r;
}