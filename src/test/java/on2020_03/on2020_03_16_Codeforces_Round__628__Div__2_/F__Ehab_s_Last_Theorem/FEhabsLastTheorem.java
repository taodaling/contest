package on2020_03.on2020_03_16_Codeforces_Round__628__Div__2_.F__Ehab_s_Last_Theorem;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.TreeSet;

public class FEhabsLastTheorem {
    int req;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        for (Node node : nodes) {
            node.deg = node.next.size();
        }

        req = (int) Math.ceil(Math.sqrt(n));
        List<Node> independentSet = new ArrayList<>();
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.deg == b.deg ? a.id - b.id : Integer.compare(a.deg, b.deg));
        set.addAll(Arrays.asList(nodes));
        while (!set.isEmpty() && set.first().deg < req - 1) {
            Node head = set.pollFirst();
            independentSet.add(head);
            head.removed = true;
            for (Node e : head.next) {
                if (e.removed) {
                    continue;
                }
                e.removed = true;
                set.remove(e);
                for (Node node : e.next) {
                    if (node.removed) {
                        continue;
                    }
                    set.remove(node);
                    node.deg--;
                    set.add(node);
                }
            }
        }

        if (set.isEmpty()) {
            out.println(1);
            for (int i = 0; i < independentSet.size() && i < req; i++) {
                out.append(independentSet.get(i).id + 1).append(' ');
            }
            return;
        }

        for (Node node : nodes) {
            if (dfs(node, null, 0)) {
                break;
            }
        }

        out.println(2);
        out.println(dq.size());
        for (Node node : dq) {
            out.append(node.id + 1).append(' ');
        }
    }

    Deque<Node> dq = new ArrayDeque<>(100000);

    public boolean dfs(Node root, Node p, int depth) {
        if (root.depth != -1) {
            if (depth - root.depth >= req) {
                while (dq.peekFirst() != root) {
                    dq.removeFirst();
                }
                return true;
            }
            return false;
        }
        root.depth = depth;
        dq.addLast(root);
        for (Node node : root.next) {
            if (node.removed || node == p) {
                continue;
            }
            if (dfs(node, root, depth + 1)) {
                return true;
            }
        }
        dq.removeLast();
        return false;
    }
}


class Node {
    int id;
    boolean removed;
    int deg;
    int depth = -1;
    List<Node> next = new ArrayList<>();
}
