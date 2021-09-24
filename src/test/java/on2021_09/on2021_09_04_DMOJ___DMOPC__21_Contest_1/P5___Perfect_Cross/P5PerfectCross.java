package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P5___Perfect_Cross;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

public class P5PerfectCross {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long s = in.ri();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            int y = in.ri();
            Point pt = new Point();
            pt.x = x + y;
            pt.y = x - y;
            pt.id = i;
            pt.rt = new RangeTree(n);
            pts[i] = pt;
        }
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            int x0 = in.ri();
            int y0 = in.ri();
            qs[i] = new Query();
            int x = x0 + y0;
            int y = x0 - y0;
            qs[i].x = x;
            qs[i].y = y;
            qs[i].id = i;
        }
        debug.debug("pts", pts);
        debug.debug("qs", qs);
        solve(qs, pts.clone(), s);
        for (int i = 0; i < n; i++) {
            pts[i].y = -pts[i].y;
        }
        for (Query query : qs) {
            query.y = -query.y;
        }
        solve(qs, pts.clone(), s);
        for (int i = 0; i < n; i++) {
            pts[i].y = -pts[i].y;
        }
        for (Query query : qs) {
            query.y = -query.y;
        }
        for (int i = 0; i < q; i++) {
            qs[i].tmp = qs[i].fr;
            qs[i].fr = null;
        }
        for (int i = 0; i < n; i++) {
            long tmp = pts[i].x;
            pts[i].x = pts[i].y;
            pts[i].y = tmp;
        }
        for (Query query : qs) {
            long tmp = query.x;
            query.x = query.y;
            query.y = tmp;
        }
        solve(qs, pts.clone(), s);
        for (int i = 0; i < n; i++) {
            pts[i].y = -pts[i].y;
        }
        for (Query query : qs) {
            query.y = -query.y;
        }
        solve(qs, pts.clone(), s);
        for (Query query : qs) {
            out.append(query.tmp.a + 1).append(' ').append(query.tmp.b + 1).append(' ')
                    .append(query.fr.a + 1).append(' ').append(query.fr.b + 1).println();
        }
    }

    public void solve(Query[] qs, Point[] pts, long s) {
        for (Query q : qs) {
            q.l = q.x - s;
            q.r = q.x + s;
            q.b = q.y - s;
            q.t = q.y + s;
        }
        Arrays.sort(pts, Comparator.<Point>comparingLong(x -> x.x).thenComparingLong(x -> x.y));
        for (int i = 0; i < pts.length; i++) {
            pts[i].orderId = i;
        }
        Deque<Point> dq = new ArrayDeque<>(pts.length);
        for (int i = 0; i < pts.length; i++) {
            pts[i].rt.clear();
            dq.clear();
            for (int j = pts.length - 1; j > i; j--) {
                if (pts[j].y < pts[i].y || pts[j].x <= pts[i].x) {
                    continue;
                }

                while (!dq.isEmpty() && dq.peekLast().y >= pts[j].y) {
                    dq.removeLast();
                }
                dq.addFirst(pts[j]);
            }
            for (Point x : dq) {
                pts[i].rt.add(x.orderId);
            }
        }
        for (Query q : qs) {
            for (Point pt : pts) {
                if (!contain(q, pt)) {
                    continue;
                }
                int l = 0;
                int r = pts.length - 1;
                while (l < r) {
                    int m = (l + r + 1) / 2;
                    if (pts[m].x <= q.r) {
                        l = m;
                    } else {
                        r = m - 1;
                    }
                }
                int choice = pt.rt.floor(l);
                if (choice < 0) {
                    continue;
                }
                Point cand = pts[choice];
                if (!contain(q, cand)) {
                    continue;
                }

                Fraction f = new Fraction();
                f.a = pt.id;
                f.b = cand.id;
                f.top = cand.y - pt.y;
                f.bot = cand.x - pt.x;
                assert f.top >= 0;
                assert f.bot >= 0;

                if (q.fr == null || Fraction.compare(q.fr, f) > 0) {
                    q.fr = f;
                }
            }
        }
    }

    public boolean contain(Query q, Point pt) {
        if (pt.x < q.l || pt.x > q.r || pt.y < q.b || pt.y > q.t) {
            return false;
        }
        return true;
    }


}

class Point {
    long x;
    long y;
    int id;
    int orderId;
    RangeTree rt;

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                '}';
    }
}

class Fraction {
    long top;
    long bot;
    int a;
    int b;

    public static int compare(Fraction a, Fraction b) {
        return Long.signum(a.top * b.bot - b.top * a.bot);
    }

    @Override
    public String toString() {
        return "Fraction{" +
                "top=" + top +
                ", bot=" + bot +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}

class Query {
    long l;
    long r;
    long b;
    long t;
    long x;
    long y;
    Fraction fr;
    Fraction tmp;
    int id;

    @Override
    public String toString() {
        return "Query{" +
                "l=" + l +
                ", r=" + r +
                ", b=" + b +
                ", t=" + t +
                ", id=" + id +
                '}';
    }
}