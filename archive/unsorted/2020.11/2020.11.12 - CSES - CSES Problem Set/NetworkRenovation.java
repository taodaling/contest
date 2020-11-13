package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

public class NetworkRenovation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }
        dfs(nodes[0], null);
        Node centroid = findCentroid(nodes[0], null, nodes[0].leaf);
        PriorityQueue<Deque<Node>> pq = new PriorityQueue<>(n, (a, b) -> -Integer.compare(a.size(), b.size()));
        List<Node> buf = new ArrayList<>();
        for (Node node : centroid.adj) {
            buf.clear();
            collectLeaf(node, centroid, buf);
            pq.add(new ArrayDeque<>(buf));
        }

        List<Node> uList = new ArrayList<>(n);
        List<Node> vList = new ArrayList<>(n);
        while (!pq.isEmpty()) {
            Deque<Node> dq = pq.remove();
            uList.add(dq.removeFirst());
            if (pq.isEmpty()) {
                vList.add(centroid);
            } else {
                Deque<Node> top = pq.remove();
                vList.add(top.removeFirst());
                if (!top.isEmpty()) {
                    pq.add(top);
                }
            }
            if (!dq.isEmpty()) {
                pq.add(dq);
            }
        }
        out.println(uList.size());
        for (int i = uList.size() - 1; i >= 0; i--) {
            Node a = uList.get(i);
            Node b = vList.get(i);
            out.append(a.id + 1).append(' ').append(b.id + 1).println();
        }
    }

    public static void collectLeaf(Node root, Node p, List<Node> seq) {
        if (root.adj.size() == 1) {
            seq.add(root);
            return;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            collectLeaf(node, root, seq);
        }
    }

    public static Node findCentroid(Node root, Node p, int total) {
        int maxSub = total - root.leaf;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            maxSub = Math.max(maxSub, node.leaf);
            Node ans = findCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
        }
        if (maxSub * 2 <= total) {
            return root;
        }
        return null;
    }

    public static void dfs(Node root, Node p) {
        if (root.adj.size() == 1) {
            root.leaf++;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.leaf += node.leaf;
        }
    }


}

class Node {
    List<Node> adj = new ArrayList<>();
    int leaf;
    int id;
}
