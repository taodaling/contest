package on2021_09.on2021_09_23_AtCoder___AtCoder_Beginner_Contest_218.G___Game_on_Tree_2;



import template.datastructure.CountTreap;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.utils.LongBinaryMerger;

import java.util.ArrayList;
import java.util.List;

public class GGameOnTree2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].v = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null, new CountTreap());
        game(nodes[0], 0);
        long ans = nodes[0].middle;
        out.println(ans);
    }

    long inf = (long) 1e18;

    LongBinaryMerger[] prefer = new LongBinaryMerger[]{Math::max, Math::min};

    public void game(Node root, int op) {
        if (root.adj.isEmpty()) {
            return;
        }
        root.middle = prefer[op ^ 1].merge(inf, -inf);
        for (Node node : root.adj) {
            game(node, op ^ 1);
            root.middle = prefer[op].merge(root.middle, node.middle);
        }
    }

    public void dfs(Node root, Node p, CountTreap ct) {
        root.adj.remove(p);
        ct.update(root.v, 1);
        if (root.adj.isEmpty()) {
            long size = ct.size();
            long lower = (size + 1) / 2;
            long ceil = (size + 2) / 2;
            root.middle = (ct.kth(lower) + ct.kth(ceil)) / 2;
        } else {
            for (Node node : root.adj) {
                dfs(node, root, ct);
            }
        }
        ct.update(root.v, -1);
    }
}

class Node {
    long middle;
    int v;
    List<Node> adj = new ArrayList<>();
}
