package contest;

import template.datastructure.MultiWayIntStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CJeremyBearimy {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int n = k * 2;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.length = in.readInt();
            a.next.add(e);
            b.next.add(e);
        }

        dfsForSize(nodes[0], null);
        long minLCA = dfsForMinLCA(nodes[0], 0);

        dfsForDp(nodes[0], null);
        long good = nodes[0].dp;
        long bad = 0;
        for (int i = 0; i < n; i++) {
            bad += nodes[i].depth;
        }
        bad -= 2 * minLCA;
        out.append(good).append(' ').append(bad).append('\n');
    }

    public void dfsForDp(Node root, Edge p) {
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForDp(node, e);
            root.dp += node.dp;
            if (node.size % 2 == 1) {
                root.dp += e.length;
            }
        }
    }

    public void dfsForSize(Node root, Edge p) {
        root.size = 1;
        root.depth = p == null ? 0 : p.other(root).depth + p.length;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForSize(node, e);
            root.size += node.size;
            if (root.heavy == null ||
                    root.heavy.other(root).size < node.size) {
                root.heavy = e;
            }
        }
    }

    public long dfsForMinLCA(Node root, int sub) {
        int size = root.size - sub;
        if(size % 2 == 1){
            throw new RuntimeException();
        }
        Node heavyNode = root.heavy.other(root);
        int heavySize = heavyNode.size - sub;
        if (heavySize <= size / 2) {
            return root.depth * (long) (size / 2);
        }
        long ans = (long) (root.size - heavyNode.size) *
                root.depth;
        ans += dfsForMinLCA(heavyNode, sub + (root.size - heavyNode.size));
        return ans;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int size;
    long depth;
    Edge heavy;
    int id;
    long dp;

    @Override
    public String toString() {
        return "" + id;
    }
}

class Edge {
    Node a;
    Node b;
    int length;

    Node other(Node x) {
        return x == a ? b : a;
    }
}