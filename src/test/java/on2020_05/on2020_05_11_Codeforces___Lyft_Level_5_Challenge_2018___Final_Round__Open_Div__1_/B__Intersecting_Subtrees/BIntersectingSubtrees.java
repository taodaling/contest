package on2020_05.on2020_05_11_Codeforces___Lyft_Level_5_Challenge_2018___Final_Round__Open_Div__1_.B__Intersecting_Subtrees;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BIntersectingSubtrees {
    Node[] nodes = new Node[1000];
    boolean[] his = new boolean[1000];
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        int k1 = in.readInt();
        for (int i = 0; i < k1; i++) {
            Node node = nodes[in.readInt() - 1];
            node.tag = true;
        }

        Arrays.fill(his, false);
        int k2 = in.readInt();
        int last = -1;
        for (int i = 0; i < k2; i++) {
            int x = in.readInt() - 1;
            his[x] = true;
            last = x;
        }

        out.printf("B %d", last + 1).println().flush();

        Node my = nodes[in.readInt() - 1];
        Node nearest = dfs(my, null);

        out.printf("A %d", nearest.id + 1).println().flush();
        int other = in.readInt() - 1;

        out.printf("C %d", his[other] ? nearest.id + 1 : -1).println().flush();
    }

    public Node dfs(Node root, Node p) {
        if (root.tag) {
            return root;
        }
        for (Node node : root.next) {
            Node ans = dfs(node, root);
            if (ans != null) {
                return ans;
            }
        }
        return null;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    boolean tag;
    int id;
}
