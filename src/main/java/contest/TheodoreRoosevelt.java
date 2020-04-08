package contest;

import template.geometry.DynamicConvexHull;
import template.geometry.Point2D;
import template.io.FastInput;
import template.io.FastOutput;

public class TheodoreRoosevelt {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Point2D a = readPoint2D(in);
        Point2D b = readPoint2D(in);
        Point2D c = readPoint2D(in);
        DynamicConvexHull dch = new DynamicConvexHull(a, b, c);
        for(int i = 3; i < n; i++){
            dch.add(readPoint2D(in));
        }
        int hit = 0;
        for(int i = 0; i < m; i++){
            Point2D pt = readPoint2D(in);
            if(dch.contain(pt, true)){
                hit++;
            }
        }
        if(hit >= k){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }

    public Point2D readPoint2D(FastInput in) {
        return new Point2D(in.readInt(), in.readInt());
    }
}
