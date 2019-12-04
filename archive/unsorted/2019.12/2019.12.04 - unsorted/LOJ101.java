package contest;

import template.graph.LongHLPPBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class LOJ101 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        LongHLPPBeta dinic = new LongHLPPBeta(n + 1, m, s, t);
        for (int i = 0; i < m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            int c = in.readInt();
            dinic.expand(u, v, c);
        }
        long ans = dinic.send((long) 1e18);
        out.println(ans);
    }
}