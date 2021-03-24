package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.geometry.LineConvexHull;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT;
import template.utils.SortUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EStore {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int xr = 1;
        int yr = 1;
        int zr = 1;
        int xl = in.readInt();
        int yl = in.readInt();
        int zl = in.readInt();
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            xl = Math.min(xl, x);
            yl = Math.min(yl, y);
            zl = Math.min(zl, z);
            xr = Math.max(xr, x);
            yr = Math.max(yr, y);
            zr = Math.max(zr, z);
        }

        List<Point> list = new ArrayList<>(m + 8 * k);
        for (int i = 0; i < m; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            if (x >= xl && x <= xr && y >= yl && y <= yr && z >= zl && z <= zr) {
                out.println("INCORRECT");
                return;
            }

            list.add(new Point(x, y, z, 1));
        }

        out.println("CORRECT");
        Query[] qs = new Query[k];
        for (int i = 0; i < k; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            int tpx = Math.max(x, xr);
            int tpy = Math.max(y, yr);
            int tpz = Math.max(z, zr);
            int btx = Math.min(x, xl);
            int bty = Math.min(y, yl);
            int btz = Math.min(z, zl);

            Query q = new Query();
            q.include = xl <= x && x <= xr && yl <= y && y <= yr && zl <= z && z <= zr;
            q.points[0] = new Point(tpx, tpy, tpz, 0);
            q.points[1] = new Point(btx - 1, tpy, tpz, 0);
            q.points[2] = new Point(tpx, bty - 1, tpz, 0);
            q.points[3] = new Point(tpx, tpy, btz - 1, 0);
            q.points[4] = new Point(btx - 1, bty - 1, tpz, 0);
            q.points[5] = new Point(btx - 1, tpy, btz - 1, 0);
            q.points[6] = new Point(tpx, bty - 1, btz - 1, 0);
            q.points[7] = new Point(btx - 1, bty - 1, btz - 1, 0);
            qs[i] = q;
            for (Point pt : q.points) {
                list.add(pt);
            }
        }

        Point[] points = list.toArray(new Point[0]);
        buf = new Point[points.length];
        Arrays.sort(points, (a, b) -> {
            int c = a.x - b.x;
            if (c == 0) {
                c = a.y - b.y;
            }
            if (c == 0) {
                c = a.z - b.z;
            }
            if (c == 0) {
                c = -(a.w - b.w);
            }
            return c;
        });

        dac(points, 0, points.length - 1);
        for (Query q : qs) {
            if (q.include) {
                out.println("OPEN");
            } else if (q.get() == 0) {
                out.println("UNKNOWN");
            } else {
                out.println("CLOSED");
            }
        }
    }


    IntegerBIT bit = new IntegerBIT(100000);
    Point[] buf;

    public void dac(Point[] points, int l, int r) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(points, l, m);
        dac(points, m + 1, r);
        Arrays.sort(points, l, m + 1, (a, b) -> a.y - b.y);
        Arrays.sort(points, m + 1, r + 1, (a, b) -> a.y - b.y);
        SimplifiedDeque<Point> dq1 = new Range2DequeAdapter<>(i -> points[i], l, m);
        SimplifiedDeque<Point> dq2 = new Range2DequeAdapter<>(i -> points[i], m + 1, r);
        while (!dq1.isEmpty() || !dq2.isEmpty()) {
            if (dq2.isEmpty() || (!dq1.isEmpty() && dq1.peekFirst().y <= dq2.peekFirst().y)) {
                Point head = dq1.removeFirst();
                if (head.w == 1) {
                    bit.update(head.z, head.w);
                }
            } else {
                Point head = dq2.removeFirst();
                head.sum += bit.query(head.z);
            }
        }
        for (int i = l; i <= m; i++) {
            if (points[i].w == 1) {
                bit.update(points[i].z, -points[i].w);
            }
        }
    }
}

class Query {
    boolean include;
    //{}, {x}, {y}, {z}, {xy}, {xz}, {yz}, {xyz}
    Point[] points = new Point[8];

    public int get() {
        return points[0].sum - points[1].sum - points[2].sum - points[3].sum
                + points[4].sum + points[5].sum + points[6].sum - points[7].sum;
    }
}

class Point {
    int x;
    int y;
    int z;
    int w;
    int sum;

    public Point(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d, %d)", x, y, z, w, sum);
    }
}
