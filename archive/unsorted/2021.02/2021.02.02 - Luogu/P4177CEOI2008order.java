package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.*;

import java.util.List;

public class P4177CEOI2008order {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] weight = new int[n + m];
        IntegerArrayList a = new IntegerArrayList((int)1e5);
        IntegerArrayList b = new IntegerArrayList((int)1e5);
        IntegerArrayList rent = new IntegerArrayList((int)1e5);
        for (int i = 0; i < n; i++) {
            weight[i] = in.ri();
            int k = in.ri();
            for (int j = 0; j < k; j++) {
                int dep = in.ri() - 1 + n;
                a.add(i);
                b.add(dep);
                rent.add(in.ri());
            }
        }
        for (int i = n; i < n + m; i++) {
            weight[i] = -in.ri();
        }
        boolean[] pick = new boolean[n + m];
        long ans = new IntegerMaximumCloseSubGraphAdapter(new IntegerISAP())
                .maximumCloseSubGraph(a.toArray(), b.toArray(), rent.toArray(), weight, pick);
        out.println(ans);
    }
}
