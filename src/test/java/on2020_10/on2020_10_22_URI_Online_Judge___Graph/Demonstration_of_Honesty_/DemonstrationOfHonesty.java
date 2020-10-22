package on2020_10.on2020_10_22_URI_Online_Judge___Graph.Demonstration_of_Honesty_;



import template.algo.MatroidIndependentSet;
import template.algo.MatroidIntersect;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.LongDiscreteMap;

import java.io.PrintWriter;

public class DemonstrationOfHonesty {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        if(!in.hasMore()){
            throw new UnknownError();
        }

        out.printf("Instancia %d\n", testNumber);
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[][] edges = new int[2][m];
        int[] colors = new int[m];
        for (int i = 0; i < m; i++) {
            edges[0][i] = in.readInt() - 1;
            edges[1][i] = in.readInt() - 1;
            colors[i] = in.readInt();
        }
        IntegerDiscreteMap.discrete(colors);
        MatroidIntersect mi = new MatroidIntersect(m);
        boolean[] ans = mi.intersect(MatroidIndependentSet.ofColor(colors),
                 MatroidIndependentSet.ofSpanningTree(n, edges));
        int size = 0;
        for(boolean x : ans){
            if(x){
                size++;
            }
        }
        assert size <= n - 1;
        if(size == n - 1){
            out.println("sim");
        }else{
            out.println("nao");
        }
        out.println();
    }
}
