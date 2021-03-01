package contest;

import template.algo.BinarySearch;
import template.algo.DoubleTernarySearch;
import template.geometry.geo2.Circle2;
import template.geometry.geo2.Line2;
import template.geometry.geo2.Point2;
import template.geometry.old.Circle;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class Gears {
    int round = 0;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        Point2 P = new Point2(in.ri(), in.ri());
        int n = in.ri();
        Point2[] A = new Point2[n];
        for (int i = 0; i < n; i++) {
            A[i] = new Point2(in.ri(), in.ri());
        }
        Point2 Q = new Point2(in.ri(), in.ri());
        int m = in.ri();
        Point2[] B = new Point2[m];
        for (int i = 0; i < m; i++) {
            B[i] = new Point2(in.ri(), in.ri());
        }
        if (solve(P, A, Q, B) || solve(Q, B, P, A)) {
            out.println("YES");
            return;
        }
        out.println("NO");
    }

    public boolean solve(Point2 P, Point2[] A, Point2 Q, Point2[] B) {
        Circle2 c = new Circle2(P, Point2.dist(P, Q));
        List<Point2> pts = new ArrayList<>(2);
        for (Point2 pt : B) {
            Point2 delta = Point2.minus(pt, Q);
            for (int i = 0; i < A.length; i++) {
                Point2 a = Point2.minus(A[i], delta);
                Point2 b = Point2.minus(A[i + 1 == A.length ? 0 : i + 1], delta);
                Line2 line = new Line2(a, b);
                pts.clear();
                c.intersect(line, pts);
                for(Point2 cand : pts){
                    if(Point2.onSegment(a, b, cand)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
