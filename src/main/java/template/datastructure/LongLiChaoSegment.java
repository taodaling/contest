package template.datastructure;

import template.primitve.generated.datastructure.IntToLongFunction;

public class LongLiChaoSegment {
    static final long INF = Long.MAX_VALUE / 2;

    public static class Line {
        public long a;
        public long b;

        public Line(long a, long b) {
            this.a = a;
            this.b = b;
        }

        public long apply(long x) {
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

    LongLiChaoSegment left, right;
    Line line = BOTTOM;

    public LongLiChaoSegment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new LongLiChaoSegment(l, m);
            right = new LongLiChaoSegment(m + 1, r);
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

    public void update(int ll, int rr, int l, int r, Line line, IntToLongFunction func) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }

        int m = (l + r) >> 1;
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
            long mid = func.apply(m);
            if (mid >= intersect) {
                this.line = line2;
                if(left != null) {
                    left.update(ll, rr, l, m, line1, func);
                }
            } else {
                this.line = line1;
                if(right != null) {
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

    public long query(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return -INF;
        }
        if (covered(ll, rr, l, r)) {
            return line.apply(x);
        }
        int m = (l + r) >> 1;
        long ans = Math.max(left.query(ll, rr, l, m, x),
                right.query(ll, rr, m + 1, r, x));
        ans = Math.max(ans, line.apply(x));
        return ans;
    }
}
