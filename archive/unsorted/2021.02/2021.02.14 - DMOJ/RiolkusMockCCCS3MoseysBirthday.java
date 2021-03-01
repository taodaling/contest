package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class RiolkusMockCCCS3MoseysBirthday {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].v = in.ri();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (dsu.find(a) != dsu.find(b)) {
                dsu.merge(a, b);
                nodes[a].adj.add(nodes[b]);
                nodes[b].adj.add(nodes[a]);
            }
        }

        List<Node> sortByV = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            col.clear();
            dfsForCollect(nodes[i], null);
            sortByV.clear();
            sortByV.addAll(col);
            sortByV.sort(Comparator.comparingInt(x -> x.v));
            col.sort(Comparator.comparingInt(x -> x.id));
            for (int j = 0; j < sortByV.size(); j++) {
                col.get(j).target = sortByV.get(j).v;
            }
            for (Node node : col) {
                while (!node.deleted) {
                    Node leaf = findAnyLeaf(node, null);
                    path.clear();
                    findPath(leaf, null, leaf.target);
                    Node cur = path.removeLast();
                    while (!path.isEmpty()) {
                        Node last = path.removeLast();
                        apply(last, cur);
                        cur = last;
                    }
                    assert cur == leaf;
                    cur.deleted = true;
                }
            }
        }

        out.println(ops.size());
        for (Node node : nodes) {
            out.append(node.v).append(' ');
        }
        out.println();
        for (int[] op : ops) {
            out.append(op[0] + 1).append(' ').append(op[1] + 1).println();
        }
    }

    List<int[]> ops = new ArrayList<>((int) 1e6);

    void apply(Node a, Node b) {
        ops.add(new int[]{a.id, b.id});
        int tmp = a.v;
        a.v = b.v;
        b.v = tmp;
    }

    List<Node> col = new ArrayList<>();

    Node findAnyLeaf(Node root, Node p) {
        for (Node node : root.adj) {
            if (node == p || node.deleted) {
                continue;
            }
            return findAnyLeaf(node, root);
        }
        return root;
    }

    Deque<Node> path = new ArrayDeque<>();

    boolean findPath(Node root, Node p, int target) {
        path.addLast(root);
        if (root.v == target) {
            return true;
        }
        for (Node node : root.adj) {
            if (node == p || node.deleted) {
                continue;
            }
            if (findPath(node, root, target)) {
                return true;
            }
        }
        path.removeLast();
        return false;
    }

    void dfsForCollect(Node root, Node p) {
        col.add(root);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForCollect(node, root);
        }
    }
}

class Node {
    int id;
    int v;
    int target;
    boolean deleted;
    List<Node> adj = new ArrayList<>();
}