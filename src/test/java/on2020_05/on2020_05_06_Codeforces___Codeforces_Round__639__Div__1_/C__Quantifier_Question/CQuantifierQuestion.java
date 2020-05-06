package on2020_05.on2020_05_06_Codeforces___Codeforces_Round__639__Div__1_.C__Quantifier_Question;



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
            a.indeg++;
            b.next.add(a);
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
            if (node.indeg == 0) {
                continue;
            }
            if (dp(node) == node.id) {
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

        for (Node node : root.next) {
            detectCircle(node);
        }

        root.instk = false;
    }

    public int dp(Node root) {
        if (root.minIndex == -1) {
            root.minIndex = root.id;
            for (Node node : root.next) {
                root.minIndex = Math.min(root.minIndex, dp(node));
            }
        }
        return root.minIndex;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int id;
    int indeg = 0;
    int minIndex = -1;
    boolean forAll;
    boolean visited;
    boolean instk;
}
