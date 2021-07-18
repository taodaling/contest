package template.datastructure;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongBIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PointAddRectangleSum {
    List<Point> pts;
    List<Point> qs;
    LongBIT bit;
    Point[] buf;

    public PointAddRectangleSum(int ptEvent, int queryEvent) {
        pts = new ArrayList<>(ptEvent + queryEvent * 2);
        qs = new ArrayList<>(queryEvent * 2);
    }

    public void addPoint(int x, int y, int v) {
        pts.add(new ContribPoint(x, y, 0, v));
    }

    public void query(int l, int r, int b, int t) {
        QueryPoint sub = new QueryPoint(l - 1, t, 0, b);
        QueryPoint pos = new QueryPoint(r, t, 0, b);
        pts.add(sub);
        pts.add(pos);
        qs.add(sub);
        qs.add(pos);
    }

    public long[] solve() {
        int z = 0;
        IntegerArrayList list = new IntegerArrayList(pts.size());
        for (Point pt : pts) {
            pt.z = z++;
            list.add(pt.y);
        }
        list.unique();
        for (Point pt : pts) {
            pt.y = list.binarySearch(pt.y) + 1;
            if (pt.getClass() == QueryPoint.class) {
                QueryPoint ext = (QueryPoint) pt;
                ext.yl = list.lowerBound(ext.yl) + 1;
            }
        }
        Point[] ptArray = pts.toArray(new Point[0]);
        Arrays.sort(ptArray, Comparator.<Point>comparingInt(x -> x.x).thenComparingInt(x -> x.z)
                .thenComparingInt(x -> x.y));
        bit = new LongBIT(list.size());
        buf = new Point[ptArray.length];
        dac(ptArray, 0, ptArray.length - 1);
        long[] ans = new long[qs.size() / 2];
        for (int i = 0; i < ans.length; i++) {
            QueryPoint sub = (QueryPoint) qs.get(i * 2);
            QueryPoint add = (QueryPoint) qs.get(i * 2 + 1);
            ans[i] = add.sum - sub.sum;
        }
        return ans;
    }


    private void dac(Point[] pts, int l, int r) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(pts, l, m);
        dac(pts, m + 1, r);
        int iter = l;
        for (int i = m + 1; i <= r; i++) {
            if (pts[i].getClass() != QueryPoint.class) {
                continue;
            }
            QueryPoint pt = (QueryPoint) pts[i];
            while (iter <= m && pts[iter].z <= pt.z) {
                Point head = pts[iter++];
                if (head.getClass() != ContribPoint.class) {
                    continue;
                }
                bit.update(head.y, ((ContribPoint) head).v);
            }
            pt.sum += bit.query(pt.yl, pt.y);
        }
        while (iter > l) {
            iter--;
            Point head = pts[iter];
            if (head.getClass() != ContribPoint.class) {
                continue;
            }
            bit.update(head.y, -((ContribPoint) head).v);
        }
        //merge by z
        int wpos = l;
        int lIter = l;
        int rIter = m + 1;
        while (lIter <= m || rIter <= r) {
            if (rIter > r || lIter <= m && pts[lIter].z <= pts[rIter].z) {
                buf[wpos++] = pts[lIter++];
            } else {
                buf[wpos++] = pts[rIter++];
            }
        }
        System.arraycopy(buf, l, pts, l, r - l + 1);
    }

    static class Point {
        int x;
        int y;
        int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static class ContribPoint extends Point {
        int v;

        public ContribPoint(int x, int y, int z, int v) {
            super(x, y, z);
            this.v = v;
        }

        @Override
        public String toString() {
            return "ContribPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", v=" + v +
                    '}';
        }
    }

    static class QueryPoint extends Point {
        int yl;
        long sum;

        public QueryPoint(int x, int y, int z, int yl) {
            super(x, y, z);
            this.yl = yl;
        }

        @Override
        public String toString() {
            return "QueryPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", yl=" + yl +
                    ", sum=" + sum +
                    '}';
        }
    }
}
