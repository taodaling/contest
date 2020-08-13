package template.geometry;

import template.utils.GeometryUtils;

import java.util.Arrays;
import java.util.List;

public class PointPolygon extends Polygon<Point2D> {
    public PointPolygon(List<Point2D> points) {
        super(points);
    }

    public LinePolygon asLines() {
        int n = data.size();
        Line2D[] lines = new Line2D[n];
        lines[0] = new Line2D(data.get(n - 1), data.get(0));
        for (int i = 1; i < n; i++) {
            lines[i] = new Line2D(data.get(i - 1), data.get(i));
        }
        return new LinePolygon(Arrays.asList(lines));
    }

    Point2D theNearestPointX;
    Point2D theNearestPointY;
    double nearestDistance2;

    public Segment2D theNearestPointPair() {
        //最近点对，分而治之
        int n = data.size();
        pointOrderByX = data.toArray(new Point2D[n]);
        Arrays.sort(pointOrderByX, Point2D.SORT_BY_X_AND_Y);
        pointOrderByY = data.toArray(new Point2D[n]);
        Arrays.sort(pointOrderByY, Point2D.SORT_BY_Y_AND_X);
        buf = new Point2D[4 * n];
        nearestDistance2 = GeometryUtils.INF;
        theNearestPointPair(0, n, 0);
        return new Segment2D(theNearestPointX, theNearestPointY);
    }

    private void theNearestPointPair(Point2D a, Point2D b) {
        double d = a.distance2Between(b);
        if (d < nearestDistance2) {
            nearestDistance2 = d;
            theNearestPointX = a;
            theNearestPointY = b;
        }
    }

    private Point2D[] buf;
    private Point2D[] pointOrderByX;
    Point2D[] pointOrderByY;

    public void theNearestPointPair(int from, int to, int bufFrom) {
        if (to - from <= 3) {
            for (int i = from; i < to; i++) {
                for (int j = i + 1; j < to; j++) {
                    theNearestPointPair(pointOrderByY[i], pointOrderByY[j]);
                }
            }
            return;
        }

        int bufTo = bufFrom + to - from;
        System.arraycopy(pointOrderByY, from, buf, bufFrom, to - from);

        int m = (from + to) >> 1;
        int lwpos = from;
        int rwpos = m;
        for (int i = bufFrom; i < bufTo; i++) {
            if (Point2D.SORT_BY_X_AND_Y.compare(buf[i], pointOrderByX[m]) < 0) {
                pointOrderByY[lwpos++] = buf[i];
            } else {
                pointOrderByY[rwpos++] = buf[i];
            }
        }
        theNearestPointPair(from, m, bufTo);
        theNearestPointPair(m, to, bufTo);

        lwpos = from;
        rwpos = m;
        for (int i = bufFrom; i < bufTo; i++) {
            double dx2 = GeometryUtils.pow2(buf[i].x - pointOrderByX[m].x);
            if (Point2D.SORT_BY_X_AND_Y.compare(buf[i], pointOrderByX[m]) < 0) {
                if (nearestDistance2 > dx2) {
                    pointOrderByY[lwpos++] = buf[i];
                }
            } else {
                if (nearestDistance2 < dx2) {
                    pointOrderByY[rwpos++] = buf[i];
                }
            }
        }

        for (int i = from, j = m; i < lwpos; i++) {
            int k = j - 1;
            while (j < rwpos && pointOrderByY[j].y < pointOrderByY[i].y) {
                j++;
            }
            while (k >= m && GeometryUtils.pow2(pointOrderByY[k].y - pointOrderByY[i].y) < nearestDistance2) {
                theNearestPointPair(pointOrderByY[i], pointOrderByY[k]);
                k--;
            }
            k = j;
            while (k < rwpos && GeometryUtils.pow2(pointOrderByY[k].y - pointOrderByY[i].y) < nearestDistance2) {
                theNearestPointPair(pointOrderByY[i], pointOrderByY[k]);
                k++;
            }
        }
    }
}