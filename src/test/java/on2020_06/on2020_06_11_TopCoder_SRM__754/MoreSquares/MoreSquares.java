package on2020_06.on2020_06_11_TopCoder_SRM__754.MoreSquares;



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
        int ans = 0;
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

                int cnt = 0;
                if (set.contains(new Point(x1, y1))) {
                    cnt++;
                }
                if (set.contains(new Point(x2, y2))) {
                    cnt++;
                }
                ans += cnt & 1;
            }
        }

        return ans;
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    @Override
    public boolean equals(Object obj) {
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }
}
