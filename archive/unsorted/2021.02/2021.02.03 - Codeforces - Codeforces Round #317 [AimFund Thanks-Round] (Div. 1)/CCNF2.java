package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class CCNF2 {
    public void dfsForSuccess(Node root) {
        assert root.ok;
        for (Edge e : root.adj) {
            dfs(e.other(root), e);
        }
    }

    public void dfs(Node root, Edge p) {
        if (root.ok) {
            return;
        }
        root.ok = true;
        p.prefer = root;
        for (Edge e : root.adj) {
            dfs(e.other(root), e);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSUExt dsu = new DSUExt(n);
        dsu.init();
        int[] value = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            value[i] = i;
        }
        Map<Integer, Integer> map = new HashMap<>(m);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        List<Edge> edges = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            for (int j = 0; j < k; j++) {
                int var = in.ri();
                if (map.containsKey(var)) {
                    //cool
                    int other = map.remove(var);
                    nodes[other].ok = true;
                    nodes[i].ok = true;
                    dsu.containCircle[dsu.find(other)] = true;
                    dsu.containCircle[dsu.find(i)] = true;
                    value[Math.abs(var)] = var;
                } else if (map.containsKey(-var)) {
                    //cool again
                    int other = map.remove(-var);
                    if (dsu.find(other) == dsu.find(i)) {
                        nodes[other].ok = true;
                        dsu.containCircle[dsu.find(other)] = true;
                        value[Math.abs(var)] = -var;
                    } else {
                        dsu.merge(other, i);
                        Edge edge = new Edge();
                        edge.a = var > 0 ? nodes[i] : nodes[other];
                        edge.b = var > 0 ? nodes[other] : nodes[i];
                        edge.prefer = edge.a;
                        edge.index = Math.abs(var);
                        nodes[i].adj.add(edge);
                        nodes[other].adj.add(edge);
                        edges.add(edge);
                    }
                } else {
                    map.put(var, i);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            nodes[v].ok = true;
            value[Math.abs(k)] = k;
            dsu.containCircle[dsu.find(v)] = true;
        }

        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i && !dsu.containCircle[i]) {
                out.println("NO");
                return;
            }
        }

        for (Node node : nodes) {
            if (node.ok) {
                dfsForSuccess(node);
            }
        }

        for (Edge e : edges) {
            value[e.index] = e.prefer == e.a ? e.index : -e.index;
        }
        out.println("YES");
        for (int i = 1; i <= m; i++) {
            out.append(value[i] == i ? 1 : 0);
        }
    }
}

class DSUExt extends DSU {
    boolean[] containCircle;

    public DSUExt(int n) {
        super(n);
        containCircle = new boolean[n];
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        containCircle[a] = containCircle[a] || containCircle[b];
    }
}

class Edge {
    int index;
    Node a;
    Node b;
    Node prefer;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    boolean ok;
}
