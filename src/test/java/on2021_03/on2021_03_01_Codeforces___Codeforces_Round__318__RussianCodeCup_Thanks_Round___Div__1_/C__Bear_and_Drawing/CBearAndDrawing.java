package on2021_03.on2021_03_01_Codeforces___Codeforces_Round__318__RussianCodeCup_Thanks_Round___Div__1_.C__Bear_and_Drawing;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CBearAndDrawing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfs(nodes[0], null);
        dfsDown(nodes[0], null, -1);
        out.println(valid ? "Yes" : "No");
    }


    private int detectType(int[] cnt) {
        int type = -1;
        if (cnt[4] != 0) {
            return 4;
        }
        if (cnt[0] + cnt[1] + cnt[2] == 0 && cnt[3] <= 1) {
            type = 3;
        } else if (cnt[0] + cnt[1] + cnt[2] == 0 && cnt[3] <= 2) {
            type = 2;
        } else if (cnt[0] == 0 && cnt[1] <= 1) {
            type = 1;
        } else if (cnt[0] == 0 && cnt[1] <= 2) {
            type = 0;
        } else {
            type = 4;
        }
        return type;
    }

    void dfs(Node root, Node p) {
        int[] cnt = new int[5];
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            cnt[node.type]++;
        }
        root.type = detectType(cnt);
        root.cnt = cnt;
    }

    boolean valid = false;

    void dfsDown(Node root, Node p, int pType) {
        if (pType != -1) {
            root.cnt[pType]++;
        }
        if (pType == 4) {
            return;
        }
        if (detectType(root.cnt) != 4) {
            valid = true;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            root.cnt[node.type]--;
            dfsDown(node, root, detectType(root.cnt));
            root.cnt[node.type]++;
        }
    }

}

class Node {
    //0, 1, 2, 3, 4
    //A, B, C, D, impossible
    //-1 for impossible
    int[] cnt;
    int type;
    List<Node> adj = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}