package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.DoubleCostFlowEdge;
import template.primitve.generated.graph.DoubleDijkstraMinimumCostFlow;
import template.primitve.generated.graph.DoubleFlow;
import template.primitve.generated.graph.DoubleMinimumCostFlow;
import template.utils.Debug;

import java.util.List;

public class EGoshaIsHunting {
    Debug debug = new Debug(true);

    int n;

    public int idOfMonster(int i) {
        return i;
    }

    public int idOfBall(int i) {
        return n + i;
    }

    public int idOfSrc() {
        return n + 2;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        double[] p = new double[n];
        double[] u = new double[n];
        in.populate(p);
        in.populate(u);

        List<DoubleCostFlowEdge>[] g = DoubleFlow.createCostFlow(idOfDst() + 1);
        for (int i = 0; i < n; i++) {
            DoubleFlow.addEdge(g, idOfSrc(), i, 1, 0);
            DoubleFlow.addEdge(g, idOfSrc(), i, 1, p[i] * u[i]);

            DoubleFlow.addEdge(g, idOfMonster(i), idOfBall(0), 1, -p[i]);
            DoubleFlow.addEdge(g, idOfMonster(i), idOfBall(1), 1, -u[i]);
        }
        DoubleFlow.addEdge(g, idOfBall(0), idOfDst(), a, 0);
        DoubleFlow.addEdge(g, idOfBall(1), idOfDst(), b, 0);

        DoubleMinimumCostFlow costFlow = new DoubleMinCostFlowPolynomial();
        double[] cf = costFlow.apply(g, idOfSrc(), idOfDst(), a + b);

        double ans = -cf[1];
        out.println(ans);
    }
}
