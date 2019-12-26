package on2019_12.on2019_12_26_Codeforces_Round__526__Div__1_.LUOGU3398;



import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.MultiWayIntegerStack;

public class LUOGU3398 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        MultiWayIntegerStack edges = new MultiWayIntegerStack(n, n * 2);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges.addLast(a, b);
            edges.addLast(b, a);
        }

        LcaOnTree lca = new LcaOnTree(edges, 0);
        for (int i = 0; i < q; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = in.readInt() - 1;
            int d = in.readInt() - 1;

            int lcaAB = lca.lca(a, b);
            int lcaCD = lca.lca(c, d);
            if ((lca.lca(lcaAB, c) == lcaAB || lca.lca(lcaAB, d) == lcaAB) &&
                    (lca.lca(lcaCD, a) == lcaCD || lca.lca(lcaCD, b) == lcaCD)) {
                out.println("Y");
            } else {
                out.println("N");
            }
        }
    }
}
