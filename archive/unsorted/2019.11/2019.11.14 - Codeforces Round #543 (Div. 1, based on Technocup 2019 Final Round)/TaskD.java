package contest;


import java.util.ArrayList;
import java.util.List;

import template.CollectionUtils;
import template.FastInput;
import template.FastOutput;

public class TaskD {
    List<Node> dpSolutions = new ArrayList<>(1000000);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].price = in.readInt();
            nodes[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfsForDp(nodes[1], null);
        out.println(nodes[1].dp);
        dpSolutions.add(nodes[1].max);
        for (Node node : dpSolutions) {
            node.find().selected = true;
            for (Node c : node.candidate) {
                c.find().selected = true;
            }
        }

        List<Integer> ans = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            if (nodes[i].find().selected) {
                ans.add(i);
            }
        }

        out.println(ans.size());
        for (Integer node : ans) {
            out.append(node).append(' ');
        }
    }

    public void dfsForDp(Node root, Node father) {
        root.next.remove(father);
        if (root.next.isEmpty()) {
            root.dp = root.price;
            root.max = root;
            return;
        }
        Node max = null;
        int cnt = 0;
        for (Node node : root.next) {
            dfsForDp(node, root);
            root.dp += node.dp;
            if (max == null) {
                max = node.max;
                cnt = 1;
            } else if (node.max.price == max.price) {
                Node.merge(node.max, max);
                dpSolutions.add(node.max);
                node.max.candidate = CollectionUtils.mergeHeuristically(max.candidate, node.max.candidate);
                max.candidate = new ArrayList<>();
                cnt++;
            } else if (node.max.price > max.price) {
                dpSolutions.add(max);
                max = node.max;
                cnt = 1;
            } else {
                dpSolutions.add(node.max);
            }
        }

        if (root.price == max.price) {
            max.candidate.add(root);
        }
        if (root.price < max.price) {
            if (cnt == 1) {
                max.candidate.clear();
            } else {
                dpSolutions.add(max);
            }

            root.dp -= max.price;
            root.dp += root.price;
            max = root;
        }
        root.max = max;
    }

}


class Node {
    List<Node> next = new ArrayList<>();
    long dp;
    long price;
    Node p = this;
    int rank;
    int id;
    boolean selected;
    Node max;
    List<Node> candidate = new ArrayList<>();

    Node find() {
        return p == p.p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }

    public String toString() {
        return "" + id;
    }

}
