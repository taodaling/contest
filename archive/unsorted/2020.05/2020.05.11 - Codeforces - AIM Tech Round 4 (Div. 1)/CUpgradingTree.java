package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CUpgradingTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
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
        dfsForCenter(nodes[0], null, nodes[0].size);
        if (centers.size() == 2) {
            Node a = centers.get(0);
            Node b = centers.get(1);
            a.adj.remove(b);
            b.adj.remove(a);
        }

        for (Node center : centers) {
            for (Node node : center.adj) {
                top = center;
                target = node;
                last = node;
                dfs(node, center);
                focus(target);
            }
        }

        out.println(seq.size());
        for (int[] s : seq) {
            for (int x : s) {
                out.append(x + 1).append(' ');
            }
            out.println();
        }
    }

    List<int[]> seq = new ArrayList<>(200000);

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

    List<Node> centers = new ArrayList<>();

    public void dfsForCenter(Node root, Node p, int total) {
        int max = total - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            max = Math.max(node.size, max);
            dfsForCenter(node, root, total);
        }
        if (max * 2 <= total) {
            centers.add(root);
        }
    }

    public void findCenter(Node root) {
        dfsForSize(root, null);
        centers.clear();
        dfsForCenter(root, null, root.size);
    }

    Node top;
    Node target;
    Node last;

    public void add(Node x, Node y, Node yy) {
        seq.add(new int[]{x.id, y.id, yy.id});
    }

    public void focus(Node next) {
        if(next == last){
            return;
        }
        add(top, last, next);
        last = next;
    }

    public void dfs(Node root, Node p) {
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }

        if (root != target && p != target) {
            focus(root);
            add(root, p, target);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    int size;

    @Override
    public String toString() {
        return "" + id;
    }
}