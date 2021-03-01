package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class BZOJ4998 {
    Node[] nodes;

    public void dfs(Node root, Node p) {
        root.visited = true;
        root.fa = p;
        root.depth = p == null ? 0 : p.depth + 1;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
    }

    DSUExt dsu;

    Node getTop(Node a) {
        return nodes[dsu.top[dsu.find(a.id)]];
    }

    public void merge(Node a, Node b) {
        a = getTop(a);
        b = getTop(b);
        while (a != b) {
            if (a.depth < b.depth) {
                Node tmp = a;
                a = b;
                b = tmp;
            }
            //climb
            dsu.merge(a.id, a.fa.id);
            a = getTop(a);
        }
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int p = in.ri();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        dsu = new DSUExt(n);
        dsu.func = i -> 0;
        dsu.init(n);
        List<Edge> query = new ArrayList<>(p);
        List<Edge> all = new ArrayList<>(m + p);
        for (int i = 0; i < m + p; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri() - 1];
            e.b = nodes[in.ri() - 1];
            all.add(e);
            if (dsu.find(e.a.id) == dsu.find(e.b.id)) {
                e.circle = true;
            } else {
                dsu.merge(e.a.id, e.b.id);
                e.a.adj.add(e);
                e.b.adj.add(e);
//                debug.debug("e", e);
            }
            if (i >= m) {
                query.add(e);
            }
        }

        for (Node node : nodes) {
            if (node.visited) {
                continue;
            }
            dfs(node, null);
        }

        dsu.func = i -> nodes[i].depth;
        dsu.init(n);
        for (Edge e : all) {
            if (!e.circle) {
                continue;
            }
            merge(e.a, e.b);
            e.ans = dsu.size[dsu.find(e.a.id)];
        }
        for (Edge q : query) {
            if (q.ans == 0) {
                out.println("No");
            } else {
                out.println(q.ans);
            }
        }
    }
}

class DSUExt extends DSU {
    int[] top;
    int[] size;
    IntToIntegerFunction func;

    public DSUExt(int n) {
        super(n);
        top = new int[n];
        size = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            top[i] = i;
            size[i] = 1;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        top[a] = func.apply(top[a]) < func.apply(top[b]) ? top[a] : top[b];
        size[a] += size[b];
    }
}

class Edge {
    Node a;
    Node b;
    int ans;
    boolean circle;

    Node other(Node x) {
        return x == a ? b : a;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }
}

class Node {
    int id;
    int depth;
    boolean visited;
    Node fa;
    List<Edge> adj = new ArrayList<>();

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}