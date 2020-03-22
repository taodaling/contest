package contest;

import template.algo.IntBinarySearch;
import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class UOJ007 {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int t = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            int f = in.readInt() - 1;
            long s = in.readLong();
            long p = in.readLong();
            long q = in.readLong();
            long l = in.readLong();
            Edge e = new Edge();
            e.a = nodes[i];
            e.b = nodes[f];
            e.length = s;
            nodes[i].a = p;
            nodes[i].b = q;
            nodes[i].l = l;

            e.a.next.add(e);
            e.b.next.add(e);
        }

        dfsForAncestor(nodes[0], null, 0);
        subtree = new ArrayList<>(n);
        nodes[0].dp = 0;
        dac(nodes[0]);

        for (int i = 1; i < n; i++) {
            out.println(nodes[i].dp);
//            if (nodes[i].dp == 2e18) {
//                throw new RuntimeException("" + i);
//            }
        }
    }

    List<Node> subtree;

    public void dac(Node root) {
        if (root.next.isEmpty()) {
            return;
        }

        now++;
        dfsForSize(root, null);
        Node centroid = dfsForCentroid(root, null, root.size);

        Node upTo = centroid.p;
        while (upTo != null && upTo.version == now) {
            upTo = upTo.p;
        }

        debug.debug("root", root);
        debug.debug("size", root.size);
        debug.debug("centroid", centroid);
        debug.debug("upTo", upTo);

        if (centroid.p != null && centroid.p.version == now) {
            centroid.p.next.removeIf(x -> x.other(centroid.p) == centroid);
            centroid.next.removeIf(x -> x.other(centroid) == centroid.p);
            dac(centroid.p);
        }


        for (Node node = centroid.p; node != upTo && centroid.depth - node.depth <= centroid.l; node = node.p) {
            centroid.dp = Math.min(centroid.dp, node.dp + (centroid.depth - node.depth) * centroid.a + centroid.b);
        }

        subtree.clear();
        for (Edge e : centroid.next) {
            Node node = e.other(centroid);
            dfsForCollect(node, centroid);
        }
        subtree.sort((a, b) -> -Long.compare(a.depth - a.l, b.depth - b.l));
        LongConvexHullTrick cht = new LongConvexHullTrick();
        Node cur = centroid;
        for (Node node : subtree) {
            while (cur != upTo && node.depth - cur.depth <= node.l) {
                cht.insert(cur.depth, -cur.dp);
                cur = cur.p;
            }
            if (!cht.isEmpty()) {
                node.dp = Math.min(node.dp, node.depth * node.a + node.b - cht.query(node.a));
            }
        }
        for (Edge e : centroid.next) {
            Node node = e.other(centroid);
            node.next.removeIf(x -> x.other(node) == centroid);
            dac(node);
        }
    }

    public void dfsForCollect(Node root, Node p) {
        subtree.add(root);
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForCollect(node, root);
        }
    }

    public void dfsForSize(Node root, Node p) {
        root.version = now;
        root.size = 1;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node dfsForCentroid(Node root, Node p, int total) {
        int maxSize = total - root.size;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            maxSize = Math.max(maxSize, node.size);
            Node ans = dfsForCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
        }
        if (maxSize * 2 <= total) {
            return root;
        }
        return null;
    }

    int now;

    public void dfsForAncestor(Node root, Node p, long depth) {
        root.depth = depth;
        root.p = p;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForAncestor(node, root, depth + e.length);
        }
    }
}

class Edge {
    Node a;
    Node b;

    Node other(Node x) {
        return x == a ? b : a;
    }

    long length;
}

class Node {
    List<Edge> next = new ArrayList<>();
    int size;
    long depth;
    long a;
    long b;
    long dp = (long) 2e18;
    long l;
    int id;
    int version;

    Node p;

    @Override
    public String toString() {
        return "" + id;
    }
}