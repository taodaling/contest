package on2020_03.on2020_03_09_Luogu_Online_Judge.P3376__________;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;

import java.util.List;

public class P3376 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        List<IntegerFlowEdge>[] net = IntegerFlow.createFlow(n + 1);
        for (int i = 1; i <= m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            int c = in.readInt();
            IntegerFlow.addEdge(net, u, v, c);
        }

        IntegerMaximumFlow mf = new IntegerISAP(n + 1);
        int ans = mf.apply(net, s, t, (int)2e9);
        out.println(ans);
    }
}
