package on2020_10.on2020_10_26_CSES___CSES_Problem_Set.Building_Roads;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BuildingRoads {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSU dsu = new DSU(n);
        dsu.init();
        for(int i = 0; i < m; i++){
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            dsu.merge(u, v);
        }
        int last = -1;
        List<int[]> ans = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            if(dsu.find(i) != i){
                continue;
            }
            if (last != -1) {
                ans.add(new int[]{last, i});
            }
            last = i;
        }
        out.println(ans.size());
        for(int[] pair : ans){
            out.append(pair[0] + 1).append(' ').append(pair[1] + 1).println();
        }
    }
}
