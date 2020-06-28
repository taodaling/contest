package on2020_06.on2020_06_27_Codeforces___Codeforces_Round__405__rated__Div__1__based_on_VK_Cup_2017_Round_1_.B__Bear_and_Tree_Jumps;



import netscape.security.UserTarget;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class BBearAndTreeJumps {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        cnts = new int[k];
        sum = new long[k];
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfsForHeavy(nodes[0], null);
        dfs1(nodes[0], null);

        out.println(total);
    }

    public void dfsForHeavy(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForHeavy(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }

    public State dfs1(Node root, Node p) {
        State ans = new State(k);
        ans.cnts[0] = 1;
        ans.sum[0] = 0;

        for (Node node : root.adj) {
            if (node == p || node == root.heavy) {
                continue;
            }
            dfs1(node, root);
        }

        if (root.heavy != null) {
            State next = dfs1(root.heavy, root);
            for (int i = 0; i < k; i++) {
                total += next.sum[i];
                total += DigitUtils.ceilDiv(i + 1, k) * next.cnts[i];
                if (i + 1 < k) {
                    ans.cnts[i + 1] += next.cnts[i];
                    ans.sum[i + 1] += next.sum[i];
                } else {
                    ans.cnts[0] += next.cnts[i];
                    ans.sum[0] += next.sum[i] + next.cnts[i];
                }
            }
        }

        for (Node node : root.adj) {
            if (node == p || node == root.heavy) {
                continue;
            }
            dfs2(node, root, ans, 1);
            dfs3(node, root, ans, 1);
        }

        return ans;
    }

    public void dfs2(Node root, Node p, State state, int d) {
        int jump = d / k;
        int remain = d % k;
        for (int i = 0; i < k; i++) {
            long s = state.sum[i];
            long c = state.cnts[i];
            total += c * jump + s;
            total += DigitUtils.ceilDiv(i + remain, k) * c;
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs2(node, root, state, d + 1);
        }
    }

    public void dfs3(Node root, Node p, State state, int d) {
        int jump = d / k;
        int remain = d % k;
        state.cnts[remain]++;
        state.sum[remain] += jump;

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs3(node, root, state, d + 1);
        }
    }

    int k;
    long total;
    int[] cnts;
    long[] sum;
}

class State {
    int[] cnts;
    long[] sum;

    public State(int k) {
        cnts = new int[k];
        sum = new long[k];
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int size;
    Node heavy;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
