package contest;

import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.List;

public class CColorfulTree {
    int limit = (int) 2e5;
    Node[] nodes;
    List<UndirectedEdge>[] tree;
    List<UndirectedEdge>[] vtree;
    IntegerVersionArray va;

    Debug debug = new Debug(false);

    public void add(IntegerList list, int x) {
        if (va.get(x) == 0) {
            nodes[x].sub = 0;
            va.set(x, 1);
            list.add(x);
        }
    }

    private void sort(IntegerList list) {
        list.sort((a, b) -> Integer.compare(nodes[a].dfn, nodes[b].dfn));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }

        int n = in.readInt();
        nodes = new Node[n];
        tree = Graph.createUndirectedGraph(n);
        vtree = Graph.createUndirectedGraph(n);
        va = new IntegerVersionArray(n);
        IntegerMultiWayStack stack = new IntegerMultiWayStack(n, n);
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].color = in.readInt() - 1;
            stack.addLast(nodes[i].color, i);
        }
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(tree, a, b);
        }

        LcaOnTree lca = new LcaOnTree(tree, 0);
        dfsForSize(0, -1);

        //Count all color
        long ans = pick2(n) * n;
        IntegerList list = new IntegerList(n);
        IntegerList dq = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            va.clear();
            if (stack.isEmpty(i)) {
                ans -= pick2(n);
//                debug.debug("i", i);
//                debug.debug("local", pick2(n));
                continue;
            }
            list.clear();
            //add children
            for (IntegerIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                int node = iterator.next();
                add(list, node);
                for (UndirectedEdge e : tree[node]) {
                    if (e.to == nodes[node].parent) {
                        continue;
                    }
                    add(list, e.to);
                }
            }
            add(list, 0);
            sort(list);
            for (int j = 1, size = list.size(); j < size; j++) {
                int prev = list.get(j - 1);
                int cur = list.get(j);
                add(list, lca.lca(prev, cur));
            }
            sort(list);
            dq.clear();

            for (int j = 0; j < list.size(); j++) {
                int node = list.get(j);
                while (!dq.isEmpty() && lca.lca(dq.tail(), node) != dq.tail()) {
                    dq.pop();
                }
                if (dq.isEmpty()) {
                    nodes[node].virtualParent = null;
                } else {
                    nodes[node].virtualParent = nodes[dq.tail()];
                }
                dq.add(node);
            }

            long local = 0;
            for (int j = list.size() - 1; j >= 0; j--) {
                Node node = nodes[list.get(j)];
                if (node.color == i) {
                    node.sub = node.size;
                }
                if (node.virtualParent != null) {
                    node.virtualParent.sub += node.sub;
                }
                if (node.color != i && (node.virtualParent == null || node.virtualParent.color == i)) {
                    local += pick2(node.size - node.sub);
                }
            }

            ans -= local;
//            debug.debug("i", i);
//            debug.debug("local", local);
        }

        out.printf("Case #%d: %d\n", testNumber, ans);
    }

    int order = 0;

    public void dfsForSize(int root, int p) {
        nodes[root].size = 1;
        nodes[root].parent = p;
        nodes[root].dfn = order++;
        for (UndirectedEdge e : tree[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSize(e.to, root);
            nodes[root].size += nodes[e.to].size;
        }
    }

    public long pick2(long n) {
        return n * (n - 1) / 2;
    }
}

class Node {
    int size;
    int sub;
    int color;
    int parent;
    Node virtualParent;
    int dfn;
}