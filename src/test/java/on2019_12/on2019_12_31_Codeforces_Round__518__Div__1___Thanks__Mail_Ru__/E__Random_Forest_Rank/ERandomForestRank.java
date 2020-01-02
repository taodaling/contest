package on2019_12.on2019_12_31_Codeforces_Round__518__Div__1___Thanks__Mail_Ru__.E__Random_Forest_Rank;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

import java.util.ArrayList;
import java.util.List;

public class ERandomForestRank {
    Modular mod = new Modular(998244353);
    Power power = new Power(mod);
    int inv2 = power.inverseByFermat(2);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null);
        int exp = nodes[0].exp;
        int ans = mod.mul(exp, power.pow(2, n));
        out.println(ans);
    }

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        root.prob[1] = 1;
        for (Node node : root.next) {
            dfs(node, root);
            root.prob[0] = mod.plus(root.prob[0], mod.mul(inv2, mod.mul(root.prob[1], node.prob[1])));
            root.prob[1] = mod.subtract(1, root.prob[0]);
            root.exp = mod.plus(root.exp, node.exp);
        }
        root.exp = mod.plus(root.exp, root.prob[0]);
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int id;
    int[] prob = new int[2];
    int exp;
}
