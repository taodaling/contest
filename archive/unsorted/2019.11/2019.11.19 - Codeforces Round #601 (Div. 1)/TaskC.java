package contest;

import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskC {
    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.readInt();
        Point[] points = new Point[n + 1];
        for (int i = 1; i <= n; i++) {
            points[i] = new Point();
            points[i].id = i;
        }

        List<Point> up = new ArrayList<>();
        List<Point> down = new ArrayList<>();
        for (int i = 3; i <= n; i++) {
            points[i].area = gotY(1, 2, i);
            if (isCC(1, 2, i)) {
                up.add(points[i]);
            } else {
                down.add(points[i]);
            }
        }

        List<Point> lt = new ArrayList<>();
        List<Point> rt = new ArrayList<>();
        List<Point> ld = new ArrayList<>();
        List<Point> rd = new ArrayList<>();

        if (!up.isEmpty()) {
            Point upMax = maxAreaPoint(up);
            for (Point pt : up) {
                if (pt == upMax) {
                    continue;
                }
                if (isCC(1, upMax.id, pt.id)) {
                    lt.add(pt);
                } else {
                    rt.add(pt);
                }
            }
            if (lt.isEmpty() || maxAreaPoint(lt).area < upMax.area) {
                lt.add(upMax);
            } else {
                rt.add(upMax);
            }
        }

        if (!down.isEmpty()) {
            Point downMax = maxAreaPoint(down);
            for (Point pt : down) {
                if (pt == downMax) {
                    continue;
                }
                if (isCC(1, downMax.id, pt.id)) {
                    rd.add(pt);
                } else {
                    ld.add(pt);
                }
            }
            if (ld.isEmpty() || maxAreaPoint(ld).area < downMax.area) {
                ld.add(downMax);
            } else {
                rd.add(downMax);
            }
        }

        out.println(0);
        out.append(1).append(' ');
        ld.sort((a, b) -> Long.compare(a.area, b.area));
        for (Point p : ld) {
            out.append(p.id).append(' ');
        }
        rd.sort((a, b) -> -Long.compare(a.area, b.area));
        for (Point p : rd) {
            out.append(p.id).append(' ');
        }
        out.append(2).append(' ');
        rt.sort((a, b) -> Long.compare(a.area, b.area));
        for (Point p : rt) {
            out.append(p.id).append(' ');
        }
        lt.sort((a, b) -> -Long.compare(a.area, b.area));
        for (Point p : lt) {
            out.append(p.id).append(' ');
        }
        out.flush();
    }

    public Point maxAreaPoint(List<Point> pt) {
        Point max = pt.get(0);
        for (Point p : pt) {
            if (max.area < p.area) {
                max = p;
            }
        }
        return max;
    }

    public boolean isCC(int zero, int i, int j) {
        out.printf("2 %d %d %d\n", zero, i, j);
        out.flush();
        int sign = in.readInt();
        return sign > 0;
    }

    public long gotY(int zero, int x, int j) {
        out.printf("1 %d %d %d\n", zero, x, j);
        out.flush();
        long area = in.readLong();
        return area;
    }
}

class Point {
    long area;
    int id;
}
