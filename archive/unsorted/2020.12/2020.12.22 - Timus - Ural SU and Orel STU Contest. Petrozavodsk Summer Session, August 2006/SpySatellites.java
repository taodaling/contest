package contest;

import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;
import template.problem.ClusterProblem;

public class SpySatellites {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if(n == 0){
            throw new UnknownError();
        }
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        long[] dists = new long[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                dists[i * n + j] = dists[j * n + i] = IntegerPoint2.dist2(pts[i], pts[j]);
            }
        }
        boolean[] split = ClusterProblem.cluster(n, dists);
        for(int i = 1; i <= n; i++){
            if(split[i]){
                out.append(1);
            }else{
                out.append(0);
            }
        }
        out.println();
    }
}
