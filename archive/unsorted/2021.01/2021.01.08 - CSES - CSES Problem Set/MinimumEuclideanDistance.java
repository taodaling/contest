package contest;

import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class MinimumEuclideanDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        IntegerPoint2[] ans = IntegerPoint2.theNearestPointPair(Arrays.asList(pts));
        out.println(IntegerPoint2.dist2(ans[0], ans[1]));
    }
}
