import java.util.HashSet;
import java.util.Set;

public class MoreSquares {
    public int countLocations(int N, int SX, int SY, int[] Xprefix, int[] Yprefix) {
        int[] X = new int[N];
        int[] Y = new int[N];
        int L = Xprefix.length;

        for (int i = 0; i <= L - 1; i++) {
            X[i] = Xprefix[i];
            Y[i] = Yprefix[i];
        }

        for (int i = L; i <= N - 1; i++) {
            X[i] = (X[i - 1] * 47 + 42) % SX;
            Y[i] = (Y[i - 1] * 47 + 42) % SY;
        }

        Set<Point> set = new HashSet<>(N);
        for (int i = 0; i <= N - 1; i++) {
            set.add(new Point(X[i] * 2, Y[i] * 2));
        }

        Point[] points = set.toArray(new Point[0]);
        Set<Point> ansSet = new HashSet<>(points.length * points.length);
        //debug.debug("points", points);
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                Point a = points[i];
                Point b = points[j];
                int cx = (a.x + b.x) / 2;
                int cy = (a.y + b.y) / 2;
                int dx = cx - a.x;
                int dy = cy - a.y;

                int x1 = cx + dy;
                int y1 = cy - dx;

                int x2 = cx - dy;
                int y2 = cy + dx;

                if (x1 % 2 + x2 % 2 + y1 % 2 + y2 % 2 == 0) {
                    int cnt = 0;
                    Point p1 = new Point(x1, y1);
                    Point p2 = new Point(x2, y2);
                    boolean exist1 = set.contains(p1);
                    boolean exist2 = set.contains(p2);
                    if (exist1 != exist2) {
                        if (exist1) {
                            ansSet.add(p2);
                        } else {
                            ansSet.add(p1);
                        }
                    }
                }
            }
        }

        return ansSet.size();
    }

}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int hashCode() {
        return x * 31 + y;
    }

    public boolean equals(Object obj) {
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

}
