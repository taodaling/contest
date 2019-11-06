package template;

public class LiChaoSegment implements Cloneable {
    LiChaoSegment left;
    LiChaoSegment right;
    Line line;
    double l;
    double r;
    double m;

    public static class Line {
        // y = ax + b
        double a;
        double b;

        public Line(double a, double b) {
            this.a = a;
            this.b = b;
        }

        public double y(double x) {
            return a * x + b;
        }

        //a1x+b1=a2x+b2=>(a1-a2)x=b2-b1=>x=(b2-b1)/(a1-a2)
        public static double intersectAt(Line a, Line b) {
            return (b.b - a.b) / (a.a - b.a);
        }

        @Override
        public String toString() {
            return a + "x+" + b;
        }
    }

    public static LiChaoSegment build(int l, int r, int[] vals) {
        LiChaoSegment segment = new LiChaoSegment();
        int m = (l + r) >> 1;
        segment.l = vals[l];
        segment.r = vals[r];
        segment.m = vals[m];
        if (l != r) {
            segment.left = build(l, m, vals);
            segment.right = build(m + 1, r, vals);
        }
        return segment;
    }

    public static boolean checkOutOfRange(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public static boolean checkCoverage(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    public static void update(int ll, int rr, int l, int r, Line line, LiChaoSegment segment) {
        if (checkOutOfRange(ll, rr, l, r)) {
            return;
        }
        int m = (l + r) >> 1;
        if (checkCoverage(ll, rr, l, r)) {
            if (segment.line == null) {
                segment.line = line;
                return;
            }
            Line largerA, smallerA;
            if (line.a < segment.line.a) {
                largerA = segment.line;
                smallerA = line;
            } else {
                largerA = line;
                smallerA = segment.line;
            }
            if (Math.abs(smallerA.a - largerA.a) < 1e-10) {
                if (smallerA.b >= largerA.b) {
                    segment.line = smallerA;
                } else {
                    segment.line = largerA;
                }
                return;
            }
            double x = Line.intersectAt(smallerA, largerA);
            if (x <= segment.l) {
                segment.line = largerA;
                return;
            }
            if (x >= segment.r) {
                segment.line = smallerA;
                return;
            }
            if (x <= segment.m) {
                segment.line = largerA;
                update(ll, rr, l, m, smallerA, segment.left);
            } else {
                segment.line = smallerA;
                update(ll, rr, m + 1, r, largerA, segment.right);
            }
            return;
        }
        update(ll, rr, l, m, line, segment.left);
        update(ll, rr, m + 1, r, line, segment.right);
    }

    public static LiChaoSegment updatePersistently(int ll, int rr, int l, int r, Line line, LiChaoSegment segment) {
        if (checkOutOfRange(ll, rr, l, r)) {
            return segment;
        }
        segment = segment.clone();
        int m = (l + r) >> 1;
        if (checkCoverage(ll, rr, l, r)) {
            if (segment.line == null) {
                segment.line = line;
                return segment;
            }
            Line largerA, smallerA;
            if (line.a < segment.line.a) {
                largerA = segment.line;
                smallerA = line;
            } else {
                largerA = line;
                smallerA = segment.line;
            }
            if (Math.abs(smallerA.a - largerA.a) < 1e-10) {
                if (smallerA.b >= largerA.b) {
                    segment.line = smallerA;
                } else {
                    segment.line = largerA;
                }
                return segment;
            }
            double x = Line.intersectAt(smallerA, largerA);
            if (x <= segment.l) {
                segment.line = largerA;
                return segment;
            }
            if (x >= segment.r) {
                segment.line = smallerA;
                return segment;
            }
            if (x <= segment.m) {
                segment.line = largerA;
                segment.left = updatePersistently(ll, rr, l, m, smallerA, segment.left);
            } else {
                segment.line = smallerA;
                segment.right = updatePersistently(ll, rr, m + 1, r, largerA, segment.right);
            }
            return segment;
        }
        segment.left = updatePersistently(ll, rr, l, m, line, segment.left);
        segment.right = updatePersistently(ll, rr, m + 1, r, line, segment.right);
        return segment;
    }

    public double eval(double x) {
        return line == null ? Double.MIN_VALUE : line.y(x);
    }

    public static double query(int x, int l, int r, DiscreteMap map, LiChaoSegment segment) {
        if (checkOutOfRange(x, x, l, r)) {
            return Double.MIN_VALUE;
        }
        if (l == r) {
            return segment.eval(map.val[x]);
        }
        int m = (l + r) >> 1;
        return Math.max(Math.max(
                query(x, l, m, map, segment.left),
                query(x, m + 1, r, map, segment.right)),
                segment.eval(map.val[x]));
    }

    @Override
    public LiChaoSegment clone() {
        try {
            return (LiChaoSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}