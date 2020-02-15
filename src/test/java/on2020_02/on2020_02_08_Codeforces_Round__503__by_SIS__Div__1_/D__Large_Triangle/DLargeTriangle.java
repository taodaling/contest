package on2020_02.on2020_02_08_Codeforces_Round__503__by_SIS__Div__1_.D__Large_Triangle;



import template.algo.IntBinarySearch;
import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DLargeTriangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long s = in.readLong() * 2;

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point();
            points[i].x = in.readInt();
            points[i].y = in.readInt();
        }

        List<Vector> allVectors = new ArrayList<>(n * n / 2);
        List<Vector> events = allVectors;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Vector parallel = new Vector(points[i], points[j]);
                allVectors.add(parallel);
            }
        }

        allVectors.sort(Comparator.naturalOrder());
        //events.sort(Comparator.naturalOrder());

        Point[] ptsSortByArea = points.clone();
        Arrays.sort(ptsSortByArea, (a, b) -> Math.abs(a.y - b.y));
        for (int i = 0; i < n; i++) {
            ptsSortByArea[i].index = i;
        }

        Range2DequeAdapter<Vector> eventsDeque = new Range2DequeAdapter<Vector>(i -> events.get(i),
                0, events.size() - 1);
        for (Vector vector : allVectors) {
            Point pt1 = vector.a;
            Point pt2 = vector.b;
            while (!eventsDeque.isEmpty() && eventsDeque.peekFirst().compareTo(vector) < 0) {
                Vector event = eventsDeque.removeFirst();
                handleEvent(ptsSortByArea, event.a, pt1, pt2);
                handleEvent(ptsSortByArea, event.b, pt1, pt2);
            }
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    long area = area(pt1, pt2, ptsSortByArea[mid]);
                    return area >= s;
                }
            };
            int index1 = ibs.binarySearch(0, ptsSortByArea.length - 1);
            IntBinarySearch ibs2 = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    long area = area(pt1, pt2, ptsSortByArea[mid]);
                    return area >= -s;
                }
            };
            int index2 = ibs2.binarySearch(0, ptsSortByArea.length - 1);
            if (area(pt1, pt2, ptsSortByArea[index2]) == -s) {
                index1 = index2;
            }
            if (Math.abs(area(pt1, pt2, ptsSortByArea[index1])) == s) {
                out.println("Yes");
                for (Point pt : SequenceUtils.wrapObjectArray(pt1, pt2, ptsSortByArea[index1])) {
                    out.append(pt.x).append(' ').append(pt.y).println();
                }
                return;
            }
        }

        out.println("No");
    }

    public long area(Point pt1, Point pt2, Point pt3) {
        return Vector.cross(pt2.x - pt1.x, pt2.y - pt1.y,
                pt3.x - pt1.x, pt3.y - pt1.y);
    }

    public void handleEvent(Point[] data, Point pt, Point pt1, Point pt2) {
        long area = area(pt1, pt2, pt);
        while (pt.index > 0 &&
                area < area(pt1, pt2, data[pt.index - 1])) {
            swap(data, pt.index, pt.index - 1);
        }
        while (pt.index + 1 < data.length &&
                area > area(pt1, pt2, data[pt.index + 1])) {
            swap(data, pt.index, pt.index + 1);
        }
    }

    public void swap(Point[] data, int i, int j) {
        Point tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        data[i].index = i;
        data[j].index = j;
    }
}

class Point {
    int x;
    int y;
    int index;

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}

class Vector implements Comparable<Vector> {
    int x;
    int y;

    Point a;
    Point b;


    public Vector(Point a, Point b) {
        if (b.y < a.y || b.y == a.y && b.x < a.x) {
            Point tmp = a;
            a = b;
            b = tmp;
        }
        this.a = a;
        this.b = b;
        this.x = b.x - a.x;
        this.y = b.y - a.y;
    }

    public static long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - x2 * y1;
    }

    @Override
    public int compareTo(Vector o) {
        return -Long.signum(cross(x, y, o.x, o.y));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
