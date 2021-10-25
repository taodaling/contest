package on2021_10.on2021_10_08_AtCoder___AtCoder_Beginner_Contest_215.F___Dist_Max_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerGenericBIT;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;

public class FDistMax2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.ri();
            pts[i].y = in.ri();
        }

        long ans = 0;
        for(int flip = 0; flip <= 1; flip++){
            for(int neg = 0; neg <= 1; neg++){
                if(flip == 1){
                    for(Point pt : pts){
                        int tmp = pt.x;
                        pt.x = pt.y;
                        pt.y = tmp;
                    }
                }

                if(neg == 1){
                    for(Point pt : pts){
                        pt.x = -pt.x;
                    }
                }


                long cand = solve(pts);
                ans = Math.max(cand, ans);

                if(neg == 1){
                    for(Point pt : pts){
                        pt.x = -pt.x;
                    }
                }

                if(flip == 1){
                    for(Point pt : pts){
                        int tmp = pt.x;
                        pt.x = pt.y;
                        pt.y = tmp;
                    }
                }
            }
        }

        out.println(ans);
    }


    int L = (int) 3e5;
    int inf = (int) 2e9;
    IntegerArrayList allX = new IntegerArrayList(L);
    IntegerArrayList allY = new IntegerArrayList(L);
    IntegerGenericBIT minBIT = new IntegerGenericBIT(L, Math::min, inf);
    IntegerGenericBIT maxBIT = new IntegerGenericBIT(L, Math::max, -inf);
    Debug debug = new Debug(true);
    public long solve(Point[] pts) {
        //rotate
        allX.clear();
        allY.clear();
        minBIT.clear();
        maxBIT.clear();
        debug.elapse("clear");
        for (Point pt : pts) {
            pt.rx = pt.x - pt.y;
            pt.ry = pt.x + pt.y;
            allX.add(pt.rx);
            allY.add(pt.ry);
        }

        allX.unique();
        allY.unique();
        for (Point pt : pts) {
            pt.dx = allX.binarySearch(pt.rx) + 1;
            pt.dy = allY.binarySearch(pt.ry) + 1;
        }
        debug.elapse("unique");
        SortUtils.radixSortIntObject(pts, 0, pts.length - 1, x -> x.dx);
        debug.elapse("sort pts");
        long best = 0;
        for (Point pt : pts) {
            best = Math.max(best, (long)maxBIT.query(pt.dy) - pt.y);
            best = Math.max(best, (long)pt.y - minBIT.query(pt.dy));
            maxBIT.update(pt.dy, pt.y);
            minBIT.update(pt.dy, pt.y);
        }
        debug.elapse("bit");
        return best;
    }
}

class Point {
    int x;
    int y;
    int rx;
    int ry;
    int dx;
    int dy;
}