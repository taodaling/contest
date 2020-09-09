package on2020_09.on2020_09_07_Codeforces___Codeforces_Round__666__Div__1_.E__Distance_Matching;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class EDistanceMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfsForSize(nodes[0], null);
        Node root = dfsForCentroid(nodes[0], null, n);
        dfsForDepth(root, null);
        dfsForSize(root, null);

        long sumOfDepth = 0;
        for (Node node : nodes) {
            sumOfDepth += node.depth;
        }

        long max = sumOfDepth;
        long min = 0;
        for (Node node : nodes) {
            min += node.size % 2;
        }

        if (min % 2 != k % 2 || min > k || k > max) {
            out.println("NO");
            return;
        }
        out.println("YES");

        long cur = max;
        long target = k;
        PriorityQueue<TreeSet<Node>> pq = new PriorityQueue<>((a, b) -> -Integer.compare(a.size(), b.size()));
        for (Node node : root.adj) {
            TreeSet<Node> set = new TreeSet<>(Node.sortByDepth);
            collect(node, root, set);
            pq.add(set);
        }

        while (cur > target) {
            TreeSet<Node> top = pq.remove();
            Node leaf = top.last();
            leaf = leaf.p;
            while (cur - leaf.depth * 2 < target) {
                leaf = leaf.p;
            }

            cur -= leaf.depth * 2;
            Node a = pop(leaf.adj);
            Node b = pop(leaf.adj);
            if (b == null) {
                b = leaf;
            }
            a.mate = b;
            b.mate = a;
            top.remove(a);
            top.remove(b);

            if (!top.isEmpty()) {
                pq.add(top);
            }
        }

        TreeSet<Node> rootSet = new TreeSet<>(Node.sortByDepth);
        rootSet.add(root);
        pq.add(rootSet);
        while (!pq.isEmpty()) {
            TreeSet<Node> a = pq.remove();
            TreeSet<Node> b = pq.remove();
            Node u = a.pollFirst();
            Node v = b.pollFirst();
            u.mate = v;
            v.mate = u;

            if (!a.isEmpty()) {
                pq.add(a);
            }
            if (!b.isEmpty()) {
                pq.add(b);
            }
        }

        for (Node node : nodes) {
            if (node.id > node.mate.id) {
                continue;
            }
            out.append(node.id + 1).append(' ').append(node.mate.id + 1).println();
        }
    }

    Node pop(List<Node> list) {
        while (true) {
            if (list.isEmpty()) {
                return null;
            }
            Node ans = list.remove(list.size() - 1);
            if (ans.mate == null) {
                return ans;
            }
        }
    }


    public void collect(Node root, Node p, TreeSet<Node> trace) {
        root.p = p;
        root.adj.remove(root.p);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            collect(node, root, trace);
        }
        trace.add(root);
    }

    public void dfsForDepth(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root);
        }
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node dfsForCentroid(Node root, Node p, int n) {
        int maxSub = n - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Node ans = dfsForCentroid(node, root, n);
            if (ans != null) {
                return ans;
            }
            maxSub = Math.max(maxSub, node.size);
        }
        return maxSub * 2 <= n ? root : null;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
    int size;
    Node p;
    Node mate;
    int id;

    public static Comparator<Node> sortByDepth = (a, b) -> a.depth == b.depth ? Integer.compare(a.id, b.id) : Integer.compare(a.depth, b.depth);

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}