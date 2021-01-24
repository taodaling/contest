package on2021_01.on2021_01_20_Library_Checker.K_Shortest_Walk;



import template.graph.KthSmallestPath;
import template.io.FastInput;
import template.io.FastOutput;

public class KShortestWalk {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int s = in.ri();
        int t = in.ri();
        int k = in.ri();
        KthSmallestPath ksp = new KthSmallestPath(n);
        for(int i = 0; i < m; i++){
            int a = in.ri();
            int b = in.ri();
            int c = in.ri();
            ksp.addEdge(a, b, c);
        }
        ksp.solve(k, s, t);
        for(int i = 0; i < k; i++){
            out.println(ksp.getKthLength(i));
        }
    }
}
