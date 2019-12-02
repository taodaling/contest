package on2019_12.on2019_12_02_AtCoder_Regular_Contest_101.E___Ribbons_on_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Factorial;
import template.math.Modular;
import template.math.Power;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskE {
    int[] choose2;
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        choose2 = new int[10000];
        choose2[0] = 1;
        Factorial fact = new Factorial(10000, mod);
        Composite comp = new Composite(fact);
        for (int i = 2; i < 10000; i += 2) {
            choose2[i] = mod.mul(choose2[i - 2], comp.composite(i, 2));
        }
        for (int i = 0; i < 10000; i += 2) {
            choose2[i] = mod.mul(choose2[i], fact.invFact(i / 2));
        }

        dfsForSize(nodes[1], null);
        dfs(nodes[1], null);
        int[][] dp = nodes[1].dp;

        int ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= n; j++) {
                int local = mod.mul(dp[i][j], choose2[j]);
                if (i % 2 == 1) {
                    local = mod.valueOf(-local);
                }
                ans = mod.plus(ans, local);
            }
        }

        out.println(ans);
    }

    public void dfsForSize(Node root, Node p) {
        root.next.remove(p);
        root.size = 1;
        for (Node node : root.next) {
            dfsForSize(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }

    public void dfs(Node root, int[][] dp) {
        if (dp == null) {
            dp = new int[2][root.size + 1];
        }
        root.dp = dp;
        if (root.heavy == null) {
            root.dp[0][1] = 1;
            return;
        }

        dfs(root.heavy, dp);
        int[][] last = dp;
        int[][] next = new int[2][root.size + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= root.heavy.size; j++) {
                //reserve
                next[i][j + 1] = mod.plus(next[i][j + 1], last[i][j]);
                //cut
                next[1 - i][1] = mod.valueOf(next[1 - i][1] + (long)choose2[j] * last[i][j]);
            }
        }

        {
            int[][] tmp = last;
            last = next;
            next = tmp;
        }

        int size = root.heavy.size + 1;
        for (Node node : root.next) {
            if (root.heavy == node) {
                continue;
            }
            dfs(node, null);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= size; j++) {
                    next[i][j] = 0;
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= size; j++) {
                    for (int k = 1; k <= node.size; k++) {
                        //reserve
                        next[i][j + k] = mod.valueOf(next[i][j + k] + (long)last[i][j] * node.dp[0][k]);
                        next[i][j + k] = mod.valueOf(next[i][j + k] + (long)last[1 - i][j] * node.dp[1][k]);

                        //cut
                        next[i][j] = mod.valueOf(next[i][j] + (long) last[i][j] * mod.mul(node.dp[1][k], choose2[k]));
                        next[i][j] = mod.valueOf(next[i][j] + (long)last[1 - i][j] * mod.mul(node.dp[0][k], choose2[k]));
                    }
                }
            }

            size += node.size;
            {
                int[][] tmp = last;
                last = next;
                next = tmp;
            }
        }

        if (last != dp) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= root.size; j++) {
                    dp[i][j] = last[i][j];
                }
            }
        }
    }

}

class Node {
    List<Node> next = new ArrayList<>();
    int[][] dp;
    int size;
    int id;
    Node heavy;

    @Override
    public String toString() {
        return "" + id;
    }
}
