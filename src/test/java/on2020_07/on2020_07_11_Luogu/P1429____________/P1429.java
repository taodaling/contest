package on2020_07.on2020_07_11_Luogu.P1429____________;



import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P1429 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point2[] pts = new Point2[n];
        for(int i = 0; i < n; i++){
            pts[i] = new Point2(in.readDouble(), in.readDouble());
        }
        Point2[] pair = Point2.theNearestPointPair(Arrays.asList(pts));
        double dist = Point2.dist(pair[0], pair[1]);
        out.printf("%.4f", dist);
    }
}
