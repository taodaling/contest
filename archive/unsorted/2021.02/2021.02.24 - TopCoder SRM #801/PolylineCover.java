package contest;

import template.geometry.geo2.Point2;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class PolylineCover {
    public double[] cover(int[] L, int[] X, int[] Y) {
        int offset = 0;
        double diameter = 0;
        double[] ans = new double[L.length * 6];
        int wpos = 0;
        for (int l : L) {
            int from = offset;
            int to = offset + l;
            double d = diameter(Arrays.copyOfRange(X, from, to), Arrays.copyOfRange(Y, from, to));
            offset = to;
            diameter = Math.max(diameter, d);
        }
        offset = 0;
        diameter += 10;
        for(int l : L){
            int from = offset;
            int to = offset + l;
            double[] rect = rect(Arrays.copyOfRange(X, from, to), Arrays.copyOfRange(Y, from, to), diameter, 1.9e10 / diameter);
            for(int i = 0; i < 6; i++){
                ans[wpos + i] = rect[i];
            }
            wpos += 6;
        }
        return ans;
    }

    public long dist2(long x1, long y1, long x2, long y2) {
        long dx = x1 - x2;
        long dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public double[] rect(int[] X, int[] Y, double length, double width) {
        long max = -1;
        int n = X.length;
        int d1 = -1;
        int d2 = -1;
        assert n > 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dist2 = dist2(X[i], Y[i], X[j], Y[j]);
                if (dist2 > max) {
                    max = dist2;
                    d1 = i;
                    d2 = j;
                }
            }
        }
        Point2 p1 = new Point2(X[d1], Y[d1]);
        Point2 p2 = new Point2(X[d2], Y[d2]);
        Point2 fp1 = new Point2(0, 0);
        Point2 fp2 = new Point2(Point2.dist(p1, p2), 0);
        double low = 0;
        for (int i = 0; i < n; i++) {
            Point2 cast = Point2.linearTransform(p1, fp1, p2, fp2, new Point2(X[i], Y[i]));
            low = Math.min(low, cast.y);
        }
        low -= 3;
        Point2 pt1 = new Point2(0 - 5, low);
        Point2 pt2 = new Point2(length - 5, low);
        Point2 pt3 = new Point2(length - 5, low + width);
        pt1 = Point2.linearTransform(fp1, p1, fp2, p2, pt1);
        pt2 = Point2.linearTransform(fp1, p1, fp2, p2, pt2);
        pt3 = Point2.linearTransform(fp1, p1, fp2, p2, pt3);
        double[] ans = new double[]{pt3.x, pt3.y, pt2.x, pt2.y, pt1.x, pt1.y};
        return ans;
    }

    public double diameter(int[] X, int[] Y) {
        long max = 0;
        int n = X.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                long dist2 = dist2(X[i], Y[i], X[j], Y[j]);
                max = Math.max(max, dist2);
            }
        }
        return Math.sqrt(max);
    }
}

