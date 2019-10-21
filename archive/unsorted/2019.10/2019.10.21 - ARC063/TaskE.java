package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        flag = true;

        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }
        int k = in.readInt();
        Node root = nodes[1];
        for (int i = 0; i < k; i++) {
            Node v = nodes[in.readInt()];
            v.num = in.readInt();
            if (root.num > v.num) {
                root = v;
            }
        }

        check(root, null, root.num);
        setRange(root, null);
        setValue(root, null, root.num, root.num);

        if (!flag) {
            out.println("No");
            return;
        }

        out.println("Yes");
        for (int i = 1; i <= n; i++) {
            out.println(nodes[i].num);
        }
    }

    public void setValue(Node root, Node fa, int l, int r) {
        l = Math.max(l, root.l);
        r = Math.min(r, root.r);
        root.num = r;
        for (Node node : root.next) {
            if (node == fa) {
                continue;
            }
            setValue(node, root, root.num - 1, root.num + 1);
        }
    }

    public void setRange(Node root, Node fa) {
        root.l = -(int) 1e9;
        root.r = (int) 1e9;
        if (root.num != Node.unknow) {
            root.l = root.r = root.num;
        }

        for (Node node : root.next) {
            if (node == fa) {
                continue;
            }
            setRange(node, root);
            if (root.r < node.l || root.l > node.r) {
                flag = false;
            }
            root.l = Math.max(root.l, node.l - 1);
            root.r = Math.min(root.r, node.r + 1);
        }

        if (root.l > root.r) {
            flag = false;
        }
    }

    boolean flag;

    public void check(Node root, Node fa, int depth) {
        if (root.num != Node.unknow && root.num % 2 != depth % 2) {
            flag = false;
        }
        for (Node node : root.next) {
            if (node == fa) {
                continue;
            }
            check(node, root, depth + 1);
        }
    }
}


class Node {
    public static int unknow = (int) 1e9;
    List<Node> next = new ArrayList<>();
    int num = unknow;

    int l;
    int r;
}
