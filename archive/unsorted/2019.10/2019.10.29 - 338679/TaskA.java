package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 2; i <= n; i++) {
            nodes[in.readInt()].next.add(nodes[i]);
        }

        for (int i = 1; i <= n; i++) {
            nodes[i].exp = in.readInt();
        }

        dfs(nodes[1]);
        out.println(valid ? "POSSIBLE" : "IMPOSSIBLE");
    }

    boolean valid = true;

    public void dfs(Node root) {
        int total = 0;
        for (Node node : root.next) {
            dfs(node);
            total += node.a + node.b;
        }

        int m = root.next.size();
        boolean[][] possible = new boolean[m + 1][root.exp + 1];
        possible[0][0] = true;
        for (int j = 1; j <= m; j++) {
            Node node = root.next.get(j - 1);
            for (int i = root.exp; i >= 0; i--) {
                if (i - node.a >= 0) {
                    possible[j][i] = possible[j][i] || possible[j - 1][i - node.a];
                }
                if (i - node.b >= 0) {
                    possible[j][i] = possible[j][i] || possible[j - 1][i - node.b];
                }
            }
        }

        int max = -1;
        for (int i = root.exp; i >= 0; i--) {
            if (possible[m][i]) {
                max = i;
                break;
            }
        }

        if (max == -1) {
            valid = false;
            return;
        }

        root.a = root.exp;
        root.b = total - max;
    }
}


class Node {
    int id;

    int a;
    int b;
    List<Node> next = new ArrayList<>();
    int exp;
}
