package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToDoubleFunction;
import template.primitve.generated.datastructure.IntegerList;

public class AirportCoffee {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long l = in.readLong();
        int a = in.readInt();
        int b = in.readInt();
        int t = in.readInt();
        int r = in.readInt();

        int n = in.readInt();
        double[] pts = new double[n + 1];
        for (int i = 0; i < n; i++) {
            pts[i] = in.readLong();
        }
        if (n == 0 || pts[n - 1] != l) {
            pts[n] = l;
            n++;
        }

        IntToDoubleFunction func = i -> pts[i];
        int left = 0;
        int right = n - 1;
        DoubleLiChaoSegment lc = new DoubleLiChaoSegment(left, right);
        lc.update(left, right, left, right, negate(new DoubleLiChaoSegment.Line(1.0 / a, 0, -1)), func);
        int sep1 = 0;
        int sep2 = 0;
        int[] last = new int[n];
        for (int i = 0; i < n; i++) {
            double x0 = pts[i];
            DoubleLiChaoSegment.Line line = lc.query(i, i, left, right, x0);
            last[i] = line.id;
            double t0 = -line.apply(x0);
            sep1 = Math.max(sep1, i);
            while (sep1 + 1 < n && pts[sep1 + 1] <= x0 + a * t) {
                sep1++;
            }
            sep2 = Math.max(sep2, sep1);
            while (sep2 + 1 < n && pts[sep2 + 1] <= x0 + a * t + b * r) {
                sep2++;
            }
            lc.update(sep1 + 1, sep2, left, right, negate(new DoubleLiChaoSegment.Line(1.0 / b, t0 + t - (x0 + a * t) / b, i)), func);
            lc.update(sep2 + 1, right, left, right, negate(new DoubleLiChaoSegment.Line(1.0 / a, t0 + t + r - (x0 + a * t + b * r) / a, i)), func);
        }

        IntegerList list = new IntegerList(n);
        for (int i = n - 1; last[i] >= 0; i = last[i]) {
            list.add(last[i]);
        }
        list.reverse();
        out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.append(list.get(i)).append(' ');
        }
    }

    public DoubleLiChaoSegment.Line negate(DoubleLiChaoSegment.Line line) {
        line.a = -line.a;
        line.b = -line.b;
        return line;
    }
}

class DoubleLiChaoSegment {
    static final double INF = 1e50;

    public static class Line {
        public double a;
        public double b;
        public int id;

        public Line(double a, double b, int id) {
            this.a = a;
            this.b = b;
            this.id = id;
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

    private static final Line BOTTOM = new Line(0, -INF, -1);

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

    public Line max(Line a, Line b, double x) {
        return a.apply(x) < b.apply(x) ? b : a;
    }

    public Line query(int ll, int rr, int l, int r, double x) {
        if (noIntersection(ll, rr, l, r)) {
            return BOTTOM;
        }
        if (covered(ll, rr, l, r)) {
            return line;
        }
        int m = DigitUtils.floorAverage(l, r);
        Line ans = max(left.query(ll, rr, l, m, x),
                right.query(ll, rr, m + 1, r, x), x);
        ans = max(ans, line, x);
        return ans;
    }
}
