package on2020_08.on2020_08_05_AtCoder___AtCoder_Grand_Contest_029.E___Wandering_TKHS0;




import template.datastructure.LeftistTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EWanderingTKHS {
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

        nodes[0].maxToMax = nodes[0];
        nodes[0].max = nodes[0];

        for (Node node : nodes[0].adj) {
            dfs(node, nodes[0], nodes[0], node);
        }
        solve(nodes[0], null, 0);

        for (int i = 1; i < n; i++) {
            out.println(nodes[i].cnt);
        }
    }


    public void dfs(Node root, Node p, Node max, Node second) {
        root.p = p;
        root.max = max;
        if (root.id > max.id) {
            root.max = root;
        }
        if (root == second) {
            root.maxToMax = root;
        } else {
            root.maxToMax = root.p.maxToMax;
            if (root.id >= root.maxToMax.id) {
                root.maxToMax = root;
            }
        }
        if (max.id <= root.id) {
            root.add++;
        } else {
            if (max.p != null && root.maxToMax.id <= max.p.max.id) {
                max.add++;
            } else {
                second.add++;
            }
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (max.id > root.id) {
                dfs(node, root, max, second);
            } else {
                dfs(node, root, root, node);
            }
        }
    }

    public void solve(Node root, Node p, int cnt) {
        cnt += root.add;
        root.cnt = cnt;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            solve(node, root, cnt);
        }
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    int cnt;
    int add;
    Node p;
    Node maxToMax;
    Node max;

    @Override
    public String toString() {
        return String.format("%d=%d", id, add);
    }
}

