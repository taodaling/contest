package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToDoubleFunction;

public class DoubleLiChaoSegment {
    static final double INF = 1e50;

    public static class Line {
        public double a;
        public double b;

        public Line(double a, double b) {
            this.a = a;
            this.b = b;
        }

        public double apply(double x) {
            return a * x + b;
        }

        @Override
        public boolean equals(Object obj) {
            Line line = (Line) obj;
            return line.a == a && line.b == b;
        }

        public static double intersectAt(Line a, Line b) {
            //a1 x + b1 = a2 x + b2
            return (double) (b.b - a.b) / (a.a - b.a);
        }
    }


    public void pushUp() {
    }

    public void pushDown() {
    }

    private static final Line BOTTOM = new Line(0, -INF);

    DoubleLiChaoSegment left, right;
    Line line = BOTTOM;

    public DoubleLiChaoSegment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new DoubleLiChaoSegment(l, m);
            right = new DoubleLiChaoSegment(m + 1, r);
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

    public void update(int ll, int rr, int l, int r, Line line, IntToDoubleFunction func) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }

        int m = DigitUtils.floorAverage(l, r);
        if (covered(ll, rr, l, r)) {
            pushDown();
            Line line1 = this.line;
            Line line2 = line;
            if (line1.a > line2.a) {
                Line tmp = line1;
                line1 = line2;
                line2 = tmp;
            }
            if (line1.a == line2.a) {
                this.line = line1.b >= line2.b ? line1 : line2;
                return;
            }
            double intersect = Line.intersectAt(line1, line2);
            double mid = func.apply(m);
            if (mid >= intersect) {
                this.line = line2;
                if (left != null) {
                    left.update(ll, rr, l, m, line1, func);
                }
            } else {
                this.line = line1;
                if (right != null) {
                    right.update(ll, rr, m + 1, r, line2, func);
                }
            }
            pushUp();
            return;
        }

        pushDown();
        left.update(ll, rr, l, m, line, func);
        right.update(ll, rr, m + 1, r, line, func);
        pushUp();
    }

    public double query(int ll, int rr, int l, int r, double x) {
        if (noIntersection(ll, rr, l, r)) {
            return -INF;
        }
        if (covered(ll, rr, l, r)) {
            return line.apply(x);
        }
        int m = DigitUtils.floorAverage(l, r);
        double ans = Math.max(left.query(ll, rr, l, m, x),
                right.query(ll, rr, m + 1, r, x));
        ans = Math.max(ans, line.apply(x));
        return ans;
    }
}
