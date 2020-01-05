package contest;

import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ENewYearAndCastleConstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
        }

        long ans = 0;
        Point[] sorted = new Point[n];
        for (int i = 0; i < n; i++) {
            sorted[i] = new Point();
        }
        for (int i = 0; i < n; i++) {
            Point center = pts[i];
            for (int j = 0; j < n; j++) {
                sorted[j].x = pts[j].x - center.x;
                sorted[j].y = pts[j].y - center.y;
            }
            SequenceUtils.swap(sorted, i, n - 1);
            ans += count(sorted, n - 1);
        }

        out.println(ans);
    }

    public long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - x2 * y1;
    }

    public long cross(Point a, Point b) {
        return cross(a.x, a.y, b.x, b.y);
    }

    public long pick(long n, int i) {
        return i == 0 ? 1 : pick(n - 1, i - 1) * n / i;
    }

    public long count(Point[] pts, int n) {
        Arrays.sort(pts, 0, n, (a, b) -> {
            if (a.y >= 0 && b.y < 0) {
                return -1;
            }
            if (a.y < 0 && b.y >= 0) {
                return 1;
            }
            return cross(a, b) >= 0 ? -1 : 1;
        });

        long ans = 0;
        int cnt = 0;
        for (int i = 0, j = 0; i < n; i++) {
            if (cnt > 0) {
                cnt--;
            }
            if (cnt == 0) {
                j = i;
            }
            while (cross(pts[i], pts[(j + 1) % n]) > 0) {
                j++;
                cnt++;
            }
            ans += pick(cnt, 3);
        }

        return pick(n, 4) - ans;
    }
}

class Point {
    long x;
    long y;

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
