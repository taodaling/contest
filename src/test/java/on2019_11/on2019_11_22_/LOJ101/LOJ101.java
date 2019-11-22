package on2019_11.on2019_11_22_.LOJ101;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

public class LOJ101 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        LongHLPP dinic = new LongHLPP(n + 1, s, t);
        for (int i = 0; i < m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            int c = in.readInt();
            dinic.addEdge(u, v, c, true);
        }
        long ans = dinic.calc();
        out.println(ans);
    }
}
