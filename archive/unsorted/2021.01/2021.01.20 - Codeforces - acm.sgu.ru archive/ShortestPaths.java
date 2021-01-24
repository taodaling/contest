package contest;

import template.graph.KthSmallestPath;
import template.io.FastInput;
import template.io.FastOutput;

public class ShortestPaths {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int s = in.ri() - 1;
        int t = in.ri() - 1;
        KthSmallestPath ksp = new KthSmallestPath(n);
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            ksp.addEdge(a, b, c);
        }
        ksp.solve(k, s, t);
        for(int i = 0; i < k; i++){
            long ans = ksp.getKthLength(i);
            if(ans == -1){
                out.println("NO");
            }else{
                out.println(ans);
            }
        }
    }
}
