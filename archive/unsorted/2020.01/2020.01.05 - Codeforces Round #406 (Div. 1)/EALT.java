package contest;

import template.graph.LcaOnTree;
import template.graph.LongMinimumCloseSubGraph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedLog2;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.List;

public class EALT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        idAlloc = m + n - 1;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i + 1;
        }

        IntegerMultiWayStack edges = new IntegerMultiWayStack(n, n * 2);
        for (int i = 1; i < n; i++) {
            int aId = in.readInt() - 1;
            int bId = in.readInt() - 1;
            Node a = nodes[aId];
            Node b = nodes[bId];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.id = i - 1;
            a.next.add(e);
            b.next.add(e);

            edges.addLast(aId, bId);
            edges.addLast(bId, aId);
        }

        LcaOnTree lcaOnTree = new LcaOnTree(edges, 0);

        int vertexNum = m + n - 1 + (n - 1) * 15;
        long[] weights = new long[vertexNum];
        for (int i = 0; i < m; i++) {
            weights[idOfCitizen(i)] = 1;
        }
        for (int i = 0; i < n - 1; i++) {
            weights[idOfEdge(i)] = -1;
        }
        subGraph = new LongMinimumCloseSubGraph(weights);
        dfs(nodes[0], null, null, 0);

        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int lca = lcaOnTree.lca(a, b);
            findLCA(nodes[a], nodes[lca].depth, i);
            findLCA(nodes[b], nodes[lca].depth, i);
        }

        subGraph.solve();
        IntegerList citizen = new IntegerList(m);
        IntegerList guard = new IntegerList(n);
        boolean[] status = subGraph.getStatus();
        for (int i = 0; i < m; i++) {
            if (!status[i]) {
                citizen.add(i);
            }
        }
        for (int i = 0; i < n - 1; i++) {
            if (status[m + i]) {
                guard.add(i);
            }
        }

        out.println(citizen.size() + guard.size());
        out.append(citizen.size()).append(' ');
        for(int i = 0; i < citizen.size(); i++){
            out.append(citizen.get(i) + 1).append(' ');
        }
        out.println();
        out.append(guard.size()).append(' ');
        for(int i = 0; i < guard.size(); i++){
            out.append(guard.get(i) + 1).append(' ');
        }
    }

    int n;
    int m;
    LongMinimumCloseSubGraph subGraph;

    public int idOfCitizen(int i) {
        return i;
    }

    public int idOfEdge(int i) {
        return m + i;
    }

    int idAlloc;

    public int nextId() {
        return ++idAlloc;
    }

    public void dfs(Node root, Node p, Edge from, int depth) {
        root.depth = depth;
        if (from != null) {
            root.jump[0] = p;
            root.dependencyIds[0] = idOfEdge(from.id);
            for (int i = 0; root.jump[i] != null; i++) {
                root.jump[i + 1] = root.jump[i].jump[i];
                if(root.jump[i + 1] != null) {
                    root.dependencyIds[i + 1] = nextId();
                    subGraph.addDependency(root.dependencyIds[i + 1], root.dependencyIds[i]);
                    subGraph.addDependency(root.dependencyIds[i + 1], root.jump[i].dependencyIds[i]);
                }
            }
        }
        for (Edge e : root.next) {
            if (e == from) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, root, e, depth + 1);
        }
    }

    public void findLCA(Node a, int targetDepth, int citizen) {
        if (a.depth == targetDepth) {
            return;
        }
        int differ = a.depth - targetDepth;
        int log = CachedLog2.floorLog(differ);
        subGraph.addDependency(idOfCitizen(citizen), a.dependencyIds[log]);
        findLCA(a.jump[log], targetDepth, citizen);
    }
}

class Edge {
    Node a;
    Node b;
    int id;

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int depth;
    Node[] jump = new Node[16];
    int[] dependencyIds = new int[16];
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
