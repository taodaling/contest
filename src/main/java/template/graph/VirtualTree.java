package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class VirtualTree {
    int[] dfn;
    int[] dfnClose;
    private List<? extends DirectedEdge>[] g;
    int order = 0;
    IntegerArrayList pend;
    int[] version;
    int round = 1;
    private LcaOnTree lca;
    int top;
    int virtualTop;
    IntegerDequeImpl dq;
    private List<Integer>[] adj;

    public VirtualTree(List<? extends DirectedEdge>[] g, int top) {
        this(g, top, new LcaOnTree(g, top));
    }

    public VirtualTree(List<? extends DirectedEdge>[] g, int top, LcaOnTree lca) {
        this.g = g;
        int n = g.length;
        dfn = new int[n];
        pend = new IntegerArrayList(n);
        version = new int[n];
        this.top = top;
        dq = new IntegerDequeImpl(n);
        dfnClose = new int[n];
        adj = Graph.createGraph(n);
        this.lca = lca;
        dfsForDfn(top, -1);
    }


    private void add(int i, int x) {
        adj[i].add(x);
    }

    public LcaOnTree getLca() {
        return lca;
    }

    private void dfsForDfn(int root, int p) {
        dfn[root] = order++;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDfn(e.to, root);
        }
        dfnClose[root] = order - 1;
    }


    private void nextRound() {
        pend.clear();
        round++;
    }

    public void active(int x) {
        if (version[x] != round) {
            pend.add(x);
            version[x] = round;
            adj[x].clear();
        }
    }

    private boolean isAncestor(int a, int b) {
        return dfn[a] <= dfn[b] && dfnClose[a] >= dfn[b];
    }

    public List<Integer>[] buildVirtualTree() {
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
        nextRound();
        return adj;
    }

    public int getVirtualTop() {
        return virtualTop;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < adj.length; i++) {
            if (version[i] != round) {
                continue;
            }
            ans.append(i).append(":\n\t");
            for (int node : adj[i]) {
                ans.append(node).append(' ');
            }
            ans.append('\n');
        }
        return ans.toString();
    }
}
