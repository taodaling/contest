package on2021_07.on2021_07_06_AtCoder___AtCoder_Beginner_Contest_207.D___Congruence_Points;



import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.GeoConstant;

import java.util.Arrays;

public class DCongruencePoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point2[] S = new Point2[n];
        for (int i = 0; i < n; i++) {
            S[i] = new Point2(in.ri(), in.ri());
        }
        Point2[] T = new Point2[n];
        for (int i = 0; i < n; i++) {
            T[i] = new Point2(in.ri(), in.ri());
        }
        if (n == 1) {
            out.println("Yes");
            return;
        }
        Point2[] dst = new Point2[n];
        Point2[] src = new Point2[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (GeoConstant.compare(Point2.dist2(S[0], S[1]), Point2.dist2(T[i], T[j])) != 0) {
                    continue;
                }
                Point2 s1 = Point2.minus(S[1], S[0]);
                Point2 tj = Point2.minus(T[j], T[i]);
                double sr = -GeoConstant.theta(s1);
                double tr = -GeoConstant.theta(tj);

                for (int k = 0; k < n; k++) {
                    dst[k] = Point2.rotate(Point2.minus(T[k], T[i]), tr);
                    src[k] = Point2.rotate(Point2.minus(S[k], S[0]), sr);
                }
                Arrays.sort(src, Point2.SORT_BY_XY);
                Arrays.sort(dst, Point2.SORT_BY_XY);
                boolean equal = true;
                for (int k = 0; k < n; k++) {
                    if (Point2.SORT_BY_XY.compare(dst[k], src[k]) != 0) {
                        equal = false;
                    }
                }
                if (equal) {
                    out.println("Yes");
                    return;
                }
            }
        }
        out.println("No");
    }
}
