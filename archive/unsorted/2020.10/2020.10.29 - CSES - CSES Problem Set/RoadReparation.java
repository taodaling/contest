package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class RoadReparation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long sum = 0;
        DSU dsu = new DSU(n);
        dsu.init();
        int[][] edges = new int[m][3];
        for(int i = 0; i < m; i++){
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
            edges[i][2] = in.readInt();
        }
        int added = 0;
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        for(int[] e : edges){
            if(dsu.find(e[0]) == dsu.find(e[1])){
                continue;
            }
            added++;
            dsu.merge(e[0], e[1]);
            sum += e[2];
        }
        if(added < n - 1){
            out.println("IMPOSSIBLE");
            return;
        }
        out.println(sum);
    }
}

