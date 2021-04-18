package on2021_04.on2021_04_17_Codeforces___Codeforces_Round__715__Div__1_.D__Swap_Pass;



import template.datastructure.DSU;
import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DSwapPass {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        PointExt[] pts = new PointExt[n];
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < n; i++) {
            pts[i] = new PointExt(in.ri(), in.ri(), in.ri() - 1);
            pts[i].id = i;
            dsu.merge(pts[i].id, pts[i].a);
        }
        List<PointExt> list = Arrays.stream(pts).filter(x -> x.id != x.a).collect(Collectors.toList());
        if(list.isEmpty()){
            out.println(0);
            return;
        }
        list.sort(Comparator.<PointExt>comparingLong(x -> x.x).thenComparingLong(x -> x.y));
        PointExt first = list.remove(0);
        list.sort(IntegerPoint2.sortByPolarAngleAround(first));
        for(int i = 0; i + 1 < list.size(); i++){
            PointExt cur = list.get(i);
            PointExt next = list.get(i + 1);
            if(dsu.find(cur.a) != dsu.find(next.a)){
                dsu.merge(cur.a, next.a);
                add(cur, next);
            }
        }
        while(first.id != first.a){
            add(first, pts[first.a]);
        }
        out.println(seq.size());
        for(int[] x : seq){
            out.append(x[0] + 1).append(' ').append(x[1] + 1).println();
        }
    }

    List<int[]> seq = new ArrayList<>();
    void add(PointExt a, PointExt b){
        seq.add(new int[]{a.id, b.id});
        int tmp = a.a;
        a.a = b.a;
        b.a = tmp;
    }
}

class PointExt extends IntegerPoint2 {
    public PointExt(long x, long y, int a) {
        super(x, y);
        this.a = a;
    }

    int a;
    int id;
}
