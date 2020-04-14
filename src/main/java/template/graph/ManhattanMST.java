package template.graph;

import template.datastructure.DSU;
import template.datastructure.Range2DequeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManhattanMST {
    Point[] points;
    int[][] mst;
    Point[] buf;
    long total;

    /**
     * All points are supposed to be distinct.
     * <br>
     * O(8nlog n)
     *
     * @param n
     * @param x
     * @param y
     */
    public ManhattanMST(int n, long[] x, long[] y) {
        points = new Point[n];
        buf = new Point[n];
        mst = new int[2][n - 1];

        for (int i = 0; i < n; i++) {
            points[i] = new Point();
            points[i].x = x[i];
            points[i].y = y[i];
            points[i].id = i;
        }

        //
        //  d1  d2
        //d0        d3
        //
        //d7        d4
        //  d6  d5
        //

        //d0 d4
        //Q.x + Q.y <= P.x + P.y
        //Q.y >= P.y
        //D1(Q,P)=P.x-Q.x-P.y+Q.y
        for (int i = 0; i < n; i++) {
            points[i].ik = points[i].y;
            points[i].ok = points[i].x + points[i].y;
            points[i].a1 = -points[i].x + points[i].y;
            points[i].a2 = -points[i].a1;
        }
        //sort by ik descending, ok ascending
        Arrays.sort(points, (a, b) -> a.ik == b.ik ? Long.compare(a.ok, b.ok) : -Long.compare(a.ik, b.ik));
        dac(points, 0, n - 1, 0, 4);

        //d1 d5
        //-(Q.x + Q.y) <= -(P.x + P.y)
        //-Q.x >= -P.x
        //D1(Q,P)=P.x-Q.x-P.y+Q.y
        for (int i = 0; i < n; i++) {
            points[i].ik = -points[i].x;
            points[i].ok = -(points[i].x + points[i].y);
            points[i].a1 = -points[i].x + points[i].y;
            points[i].a2 = -points[i].a1;
        }
        //sort by ik descending, ok ascending
        Arrays.sort(points, (a, b) -> a.ik == b.ik ? Long.compare(a.ok, b.ok) : -Long.compare(a.ik, b.ik));
        dac(points, 0, n - 1, 1, 5);

        //d2 d6
        //-(Q.y-Q.x) <= -(P.y-P.x)
        //Q.x>=P.x
        //D1(Q,P)=-P.x+Q.x-P.y+Q.y
        for (int i = 0; i < n; i++) {
            points[i].ik = points[i].x;
            points[i].ok = -(points[i].y - points[i].x);
            points[i].a1 = points[i].x + points[i].y;
            points[i].a2 = -points[i].a1;
        }
        //sort by ik descending, ok ascending
        Arrays.sort(points, (a, b) -> a.ik == b.ik ? Long.compare(a.ok, b.ok) : -Long.compare(a.ik, b.ik));
        dac(points, 0, n - 1, 2, 6);

        //d3 d7
        //(Q.y-Q.x) <= (P.y-P.x)
        //Q.y>=P.y
        //D1(Q,P)=-P.x+Q.x-P.y+Q.y
        for (int i = 0; i < n; i++) {
            points[i].ik = points[i].y;
            points[i].ok = (points[i].y - points[i].x);
            points[i].a1 = points[i].x + points[i].y;
            points[i].a2 = -points[i].a1;
        }
        //sort by ik descending, ok ascending
        Arrays.sort(points, (a, b) -> a.ik == b.ik ? Long.compare(a.ok, b.ok) : -Long.compare(a.ik, b.ik));
        dac(points, 0, n - 1, 3, 7);

        //All edges
        List<Edge> edges = new ArrayList<>(8 * n);
        for (Point pt : points) {
            for (Point next : pt.eight) {
                if (next == null) {
                    continue;
                }
                Edge e = new Edge();
                e.a = pt.id;
                e.b = next.id;
                e.w = Point.dist(pt, next);
                edges.add(e);
            }
        }
        edges.sort((a, b) -> Long.compare(a.w, b.w));
        DSU dsu = new DSU(n);
        int mstOffset = 0;
        for (Edge e : edges) {
            int a = e.a;
            int b = e.b;
            if (dsu.find(a) == dsu.find(b)) {
                continue;
            }
            dsu.merge(a, b);
            mst[0][mstOffset] = a;
            mst[1][mstOffset] = b;
            mstOffset++;
            total += e.w;
        }
    }

    public int[][] getMst() {
        return mst;
    }

    public long getTotal() {
        return total;
    }

    public void dac(Point[] pts, int l, int r, int d1, int d2) {
        if (l >= r) {
            return;
        }
        int m = (l + r) >>> 1;
        dac(pts, l, m, d1, d2);
        dac(pts, m + 1, r, d1, d2);

        //contribute 1
        Range2DequeAdapter<Point> topDq = new Range2DequeAdapter<>(i -> pts[i], l, m);
        Range2DequeAdapter<Point> botDq = new Range2DequeAdapter<>(i -> pts[i], m + 1, r);
        Point min = null;
        while (!botDq.isEmpty()) {
            Point bot = botDq.removeFirst();
            while (!topDq.isEmpty() && topDq.peekFirst().ok <= bot.ok) {
                Point top = topDq.removeFirst();
                if (min == null || top.a1 < min.a1) {
                    min = top;
                }
            }
            bot.update(min, d1);
        }

        //contribute 2
        topDq = new Range2DequeAdapter<>(i -> pts[i], l, m);
        botDq = new Range2DequeAdapter<>(i -> pts[i], m + 1, r);
        min = null;
        while (!topDq.isEmpty()) {
            Point top = topDq.removeLast();
            while (!botDq.isEmpty() && botDq.peekLast().ok >= top.ok) {
                Point bot = botDq.removeLast();
                if (min == null || bot.a2 < min.a2) {
                    min = top;
                }
            }
            top.update(min, d2);
        }

        //merge sort
        topDq = new Range2DequeAdapter<>(i -> pts[i], l, m);
        botDq = new Range2DequeAdapter<>(i -> pts[i], m + 1, r);
        for (int i = l; i <= r; i++) {
            if (botDq.isEmpty() || (!topDq.isEmpty() && topDq.peekFirst().ok <= botDq.peekFirst().ok)) {
                buf[i] = topDq.removeFirst();
            } else {
                buf[i] = botDq.removeFirst();
            }
        }
        System.arraycopy(buf, l, pts, l, r - l + 1);
    }

    private static class Edge {
        int a;
        int b;
        long w;
    }

    private static class Point {
        long x;
        long y;
        int id;

        long ok;
        long ik;
        long a1;
        long a2;

        static long dist(Point a, Point b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        }

        void update(Point pt, int d) {
            if (pt == null) {
                return;
            }
            if (eight[d] == null || dist(this, pt) < dist(this, eight[d])) {
                eight[d] = pt;
            }
        }

        Point[] eight = new Point[8];
    }
}
