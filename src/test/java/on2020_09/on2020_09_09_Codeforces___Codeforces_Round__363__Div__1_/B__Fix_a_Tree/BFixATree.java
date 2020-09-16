package on2020_09.on2020_09_09_Codeforces___Codeforces_Round__363__Div__1_.B__Fix_a_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BFixATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            nodes[i].p = nodes[in.readInt() - 1];
        }

        Set<Node> set = new HashSet<>();
        for (Node node : nodes) {
            set.add(findCircle(node));
        }


        int ans = 0;
        Node root = null;
        for (Node node : set) {
            if (node.p == node) {
                root = node;
                set.remove(root);
                break;
            }
        }

        if (root == null) {
            ans++;
            root = set.stream().findFirst().get();
            set.remove(root);
            root.p = root;
        }

        for (Node node : set) {
            ans++;
            node.p = root;
        }

        out.println(ans);
        for (Node node : nodes) {
            out.append(node.p.id + 1).append(' ');
        }

    }

    public Node findCircle(Node root) {
        if (root.visited) {
            if (root.instk) {
                return root;
            }
            return root.circle;
        }
        root.visited = true;
        root.instk = true;
        root.circle = findCircle(root.p);
        root.instk = false;
        return root.circle;
    }
}


class Node {
    Node p;
    int id;
    boolean visited;
    boolean instk;
    Node circle;
}