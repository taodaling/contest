package template.graph;

import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.List;

public class UndirectedTarjanSCC {
    private List<? extends UndirectedEdge>[] g;
    public int[] low;
    public int[] dfn;
    public int[] set;
    private IntegerDequeImpl dq;
    private int order;
    private boolean[] instk;

    public UndirectedTarjanSCC(int n) {
        low = new int[n];
        dfn = new int[n];
        set = new int[n];
        instk = new boolean[n];
        dq = new IntegerDequeImpl(n);
    }

    public void init(List<? extends UndirectedEdge>[] g) {
        this.g = g;
        Arrays.fill(dfn, 0, g.length, -1);
        order = 0;
        for (int i = 0; i < g.length; i++) {
            if (dfn[i] != -1) {
                continue;
            }
            dfs(i, null);
            assert dq.isEmpty();
        }
    }

    public int find(int i) {
        return set[i];
    }

    private void dfs(int root, UndirectedEdge p) {
        if (dfn[root] != -1) {
            return;
        }
        dfn[root] = low[root] = order++;
        instk[root] = true;
        dq.addLast(root);
        for (UndirectedEdge e : g[root]) {
            if (e.rev == p) {
                continue;
            }
            dfs(e.to, e);
            if (instk[e.to]) {
                low[root] = Math.min(low[root], low[e.to]);
            }
        }
        if (dfn[root] == low[root]) {
            while (true) {
                int tail = dq.removeLast();
                set[tail] = root;
                instk[tail] = false;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}
