package on2021_06.on2021_06_07_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.K__Sewing_Graph;



import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SortUtils;

import java.util.List;

public class KSewingGraph {
    int compare(IntegerPoint2 origin, IntegerPoint2 a, IntegerPoint2 b){
        int side = IntegerPoint2.orient(origin, a, b);
        if(side == 0){
            side = Long.compare(IntegerPoint2.dist2(b, origin), IntegerPoint2.dist2(a, origin));
        }
        return side;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        boolean[] handled = new boolean[n];
        for(int i = 0; i < n; i++){
            pts[i] = new IntegerPoint2(in.ri(), in.ri());
        }
        IntegerArrayList seq = new IntegerArrayList(n);
        int first = SortUtils.argmin(i -> pts[i], 0, n - 1, IntegerPoint2.SORT_BY_XY);
        seq.add(first);
        handled[first] = true;
        int last = first;
        while(seq.size() < n){
            int best = -1;
            for(int i = 0; i < n; i++){
                if(handled[i]){
                    continue;
                }
                if(best == -1 || compare(pts[last], pts[best], pts[i]) < 0){
                    best = i;
                }
            }
            handled[best] = true;
            seq.add(best);
            last = best;
        }
        out.println(2 * (n - 1) + 1);
        for(int x : seq.toArray()){
            out.append(x + 1).append(' ');
        }
        seq.pop();
        seq.reverse();
        for(int x : seq.toArray()){
            out.append(x + 1).append(' ');
        }
    }
}
