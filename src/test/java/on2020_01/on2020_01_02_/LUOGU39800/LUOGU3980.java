package on2020_01.on2020_01_02_.LUOGU39800;




import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.LinearProgramming;

public class LUOGU3980 {
    int n;

    public int idOfDay(int d) {
        return d;
    }

    public int idOfSrc() {
        return n + 2;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();

        double inf = Integer.MAX_VALUE;
        MinCostMaxFlow mcmf = new MinCostMaxFlow(idOfDst() + 1, idOfSrc(), idOfDst());
        for(int i = 1; i <= n; i++){
            int req = in.readInt();
            mcmf.getChannel(idOfDay(i), idOfDay(i + 1), 0)
                    .reset(inf - req, 0);
        }
        mcmf.getChannel(idOfSrc(), idOfDay(1), 0).reset(inf, 0);
        mcmf.getChannel(idOfDay(n + 1), idOfDst(), 0).reset(inf, 0);

        for(int i = 1; i <= m; i++){
            int l = in.readInt();
            int r = in.readInt();
            int cost = in.readInt();
            mcmf.getChannel(idOfDay(l), idOfDay(r + 1), cost).reset(inf, 0);
        }

        double[] ans = mcmf.send(inf);
        out.println(DigitUtils.round(ans[1]));
    }
}
