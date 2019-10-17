package contest;

import template.FastInput;
import template.FastOutput;

import java.util.*;
import java.util.stream.Collectors;

public class TaskC2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        List<Point> ptList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Point pt = new Point();
            pt.x = in.readInt();
            pt.y = in.readInt();
            pt.z = in.readInt();
            pt.id = i + 1;

            ptList.add(pt);
        }

        Map<Integer, List<Point>> groupByX = ptList.stream()
                .collect(Collectors.groupingBy((x) -> x.x));

        TreeSet<Point> wait = new TreeSet<>(Point.sortByXYZ);
        for (List<Point> sameX : groupByX.values()) {
            TreeSet<Point> xQ = new TreeSet<>(Point.sortByXYZ);
            Map<Integer, List<Point>> groupByY = sameX.stream()
                    .collect(Collectors.groupingBy((x) -> x.y));
            for (List<Point> sameY : groupByY.values()) {
                TreeSet<Point> yQ = new TreeSet<>(Point.sortByXYZ);
                Map<Integer, List<Point>> groupByZ = sameY.stream()
                        .collect(Collectors.groupingBy((x) -> x.z));
                for (List<Point> sameZ : groupByZ.values()) {
                    int m = sameZ.size();
                    int i;
                    for (i = 0; i + 1 < m; i += 2) {
                        answer(sameZ.get(i), sameZ.get(i + 1), out);
                    }
                    if (i < m) {
                        yQ.add(sameZ.get(i));
                    }
                }

                while (yQ.size() >= 2) {
                    answer(yQ.pollFirst(), yQ.pollFirst(), out);
                }

                xQ.addAll(yQ);
            }

            while (xQ.size() >= 2) {
                answer(xQ.pollFirst(), xQ.pollFirst(), out);
            }
            wait.addAll(xQ);
        }

        while(wait.size() >= 2){
            answer(wait.pollFirst(), wait.pollFirst(), out);
        }
    }

    public void answer(Point a, Point b, FastOutput out) {
        out.append(a.id).append(' ').append(b.id).append('\n');
    }


}

class Point {
    int id;
    int x;
    int y;
    int z;

    static Comparator<Point> sortByXYZ = (a, b) -> a.x != b.x ? a.x - b.x : a.y != b.y ? a.y - b.y : a.z - b.z;

    @Override
    public int hashCode() {
        int h = x;
        h = h * 31 + y;
        h = h * 31 + z;
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        Point pt = (Point) obj;
        return x == pt.x && y == pt.y && z == pt.z;
    }
}
