package contest;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.IntList;

public class FountainWalk {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        Point begin = new Point();
        begin.x = in.readInt();
        begin.y = in.readInt();

        Point end = new Point();
        end.x = in.readInt();
        end.y = in.readInt();

        int n = in.readInt();
        List<Point> pts = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            Point pt = new Point();
            pt.x = in.readInt();
            pt.y = in.readInt();
            pts.add(pt);
        }

        if (begin.x > end.x) {
            begin.x = -begin.x;
            end.x = -end.x;
            for (Point pt : pts) {
                pt.x = -pt.x;
            }
        }
        if (begin.y > end.y) {
            begin.y = -begin.y;
            end.y = -end.y;
            for (Point pt : pts) {
                pt.y = -pt.y;
            }
        }

        IntList intList = new IntList(2 * (n + 2));
        intList.add(end.x);
        intList.add(end.y);
        intList.add(begin.x);
        intList.add(begin.y);


    }
    double circle = Math.PI * 10 / 2;

    public boolean inRange(Point lb, Point rt, int x, int y) {
        return lb.x <= x && rt.x >= x && lb.y <= y && rt.y >= y;
    }
}


class Point {
    int x;
    int y;
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private double max;

    private void update(double v) {
        max = Math.max(max, v);
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {}

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, double v) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            update(v);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, v);
        right.update(ll, rr, m + 1, r, v);
        pushUp();
    }

    public double query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -1e50;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.query(ll, rr, l, m), right.query(ll, rr, m + 1, r));
    }
}
