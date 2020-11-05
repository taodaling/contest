package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class VirtualTree {
    public int[] heads;
    public int[] next;
    public int[] vals;
    int[] dfn;
    int[] dfnClose;
    public List<UndirectedEdge>[] g;
    int order = 0;
    int alloc = 0;
    boolean prepared = false;
    IntegerArrayList pend;
    int[] version;
    int round;
    public LcaOnTree lca;
    int top;
    int virtualTop;
    IntegerDequeImpl dq;

    public VirtualTree(int n, int top) {
        heads = new int[n];
        next = new int[(n - 1) * 2 + 1];
        vals = new int[next.length];
        dfn = new int[n];
        g = Graph.createGraph(n);
        pend = new IntegerArrayList(n);
        version = new int[n];
        this.top = top;
        dq = new IntegerDequeImpl(n);
        dfnClose = new int[n];
    }


    private void add(int i, int x) {
        next[alloc] = heads[i];
        vals[alloc] = x;
        heads[i] = alloc;
        alloc++;
    }

    public List<UndirectedEdge>[] getTree() {
        return g;
    }

    public LcaOnTree getLca() {
        return lca;
    }

    private void dfsForDfn(int root, int p) {
        dfn[root] = order++;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDfn(e.to, root);
        }
        dfnClose[root] = order - 1;
    }


    private void prepare() {
        if (prepared) {
            return;
        }
        prepared = true;
        dfsForDfn(top, -1);
        lca = new LcaOnTree(g, 0);
    }

    public void nextRound() {
        prepare();
        pend.clear();
        round++;
        alloc = 1;
    }

    public void active(int x) {
        if (version[x] != round) {
            pend.add(x);
            version[x] = round;
            heads[x] = 0;
        }
    }

    private boolean isAncestor(int a, int b) {
        return dfn[a] <= dfn[b] && dfnClose[a] >= dfn[b];
    }

    public void buildVirtualTree() {
        assert !pend.isEmpty();
        pend.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
        for (int i = pend.size() - 1; i >= 1; i--) {
            int cur = pend.get(i);
            int last = pend.get(i - 1);
            active(lca.lca(cur, last));
        }
        pend.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
        dq.clear();
        dq.addLast(pend.get(0));
        for (int i = 1; i < pend.size(); i++) {
            int node = pend.get(i);
            while (!isAncestor(dq.peekLast(), node)) {
                dq.removeLast();
            }
            add(dq.peekLast(), node);
            dq.addLast(node);
        }
        virtualTop = dq.peekFirst();
    }

    public int getVirtualTop() {
        return virtualTop;
    }

    public void dfs(int root, int p) {
        for (int iter = heads[root]; iter != 0; iter = next[iter]) {
            int node = vals[iter];
            dfs(node, root);
        }
    }

    public void addEdge(int u, int v) {
        Graph.addUndirectedEdge(g, u, v);
    }


    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < heads.length; i++) {
            if (version[i] != round) {
                continue;
            }
            ans.append(i).append(":\n\t");
            for (int iter = heads[i]; iter != 0; iter = next[iter]) {
                int node = vals[iter];
                ans.append(node).append(' ');
            }
            ans.append('\n');
        }
        return ans.toString();
    }
}
