package contest;

import template.primitve.generated.graph.*;

import java.util.List;

public class ShoppingSpree {
    int n;
    int m;

    public int getShirtId(int i) {
        return i;
    }

    public int getJeanId(int i) {
        return n + i;
    }

    public int getSrc() {
        return n + m;
    }

    public int getDst() {
        return getSrc() + 1;
    }

    int inf = (int) 1e9;

    public int maxValue(int k, int[] shirtValue, int[] jeansValue, int[] shirtType, int[] jeansType) {

        this.n = shirtValue.length;
        this.m = jeansValue.length;

        List<IntegerCostFlowEdge>[] g = IntegerFlow.createCostFlow(getDst() + 1);
        for (int i = 0; i < n; i++) {
            IntegerFlow.addEdge(g, getSrc(), getShirtId(i), 1, -shirtValue[i]);
            IntegerFlow.addEdge(g, getSrc(), getShirtId(i), inf, 0);
        }
        for (int i = 0; i < m; i++) {
            IntegerFlow.addEdge(g, getJeanId(i), getDst(), 1, -jeansValue[i]);
            IntegerFlow.addEdge(g, getJeanId(i), getDst(), inf, 0);
        }

        int d = shirtType.length;
        for (int i = 0; i < d; i++) {
            IntegerFlow.addEdge(g, getShirtId(shirtType[i]), getJeanId(jeansType[i]), 1, 0);
        }

        IntegerMinimumCostFlow mcf = new IntegerSpfaMinimumCostFlow(g.length);
        int[] ans = mcf.apply(g, getSrc(), getDst(), k);

        int cost = -ans[1];
        return cost;
    }
}
