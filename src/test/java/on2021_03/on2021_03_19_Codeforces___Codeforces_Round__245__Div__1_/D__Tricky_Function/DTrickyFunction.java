package on2021_03.on2021_03_19_Codeforces___Codeforces_Round__245__Div__1_.D__Tricky_Function;



import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DTrickyFunction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for(int i = 1; i < n; i++){
            a[i] += a[i - 1];
        }
        Point2[] pts = new Point2[n];
        for(int i = 0; i < n; i++){
            pts[i] = new Point2(i, a[i]);
        }
        Point2[] ans = Point2.theNearestPointPair(Arrays.asList(pts));
        int i = (int) Math.round(ans[0].x);
        int j = (int) Math.round(ans[1].x);

        long best = (long)(j - i) * (j - i) + (long)(a[j] - a[i]) * (a[j] - a[i]);
        out.println(best);
    }
}
