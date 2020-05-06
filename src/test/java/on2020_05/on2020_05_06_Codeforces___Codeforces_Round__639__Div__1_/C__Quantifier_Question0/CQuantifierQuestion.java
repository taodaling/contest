package on2020_05.on2020_05_06_Codeforces___Codeforces_Round__639__Div__1_.C__Quantifier_Question0;




import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CQuantifierQuestion {
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
            a.in.add(b);
            b.out.add(a);
        }

        for (Node node : nodes) {
            detectCircle(node);
        }

        if (circle) {
            out.println(-1);
            return;
        }


        int ans = 0;
        for (Node node : nodes) {
            if (dpOut(node) == node.id && dpIn(node) == node.id) {
                node.forAll = true;
                ans++;
            }
        }

        out.println(ans);
        for (Node node : nodes) {
            if (node.forAll) {
                out.append('A');
            } else {
                out.append('E');
            }
        }
    }

    boolean circle = false;

    public void detectCircle(Node root) {
        if (root.visited) {
            if (root.instk) {
                circle = true;
            }
            return;
        }
        root.visited = true;
        root.instk = true;

        for (Node node : root.out) {
            detectCircle(node);
        }

        root.instk = false;
    }

    public int dpOut(Node root) {
        if (root.outMinIndex == -1) {
            root.outMinIndex = root.id;
            for (Node node : root.out) {
                root.outMinIndex = Math.min(root.outMinIndex, dpOut(node));
            }
        }
        return root.outMinIndex;
    }

    public int dpIn(Node root) {
        if (root.inMinIndex == -1) {
            root.inMinIndex = root.id;
            for (Node node : root.in) {
                root.inMinIndex = Math.min(root.inMinIndex, dpIn(node));
            }
        }
        return root.inMinIndex;
    }
}

class Node {
    List<Node> out = new ArrayList<>();
    List<Node> in = new ArrayList<>();
    int id;
    int outMinIndex = -1;
    int inMinIndex = -1;
    boolean forAll;
    boolean visited;
    boolean instk;
}
