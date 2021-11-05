package on2021_10.on2021_10_30_AtCoder___AtCoder_Beginner_Contest_223.G___Vertex_Deletion;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class GVertexDeletion {
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
        int best = DigitUtils.lowBit(nodes[0].state);
        debug.debug("best", best);
        dfsUpDown(nodes[0], null, DigitUtils.asLong(0, 0));

        int ans = 0;
        for(Node node : nodes){
            if(node.max == best){
                ans++;
            }
        }
        out.println(ans);
    }

    Debug debug = new Debug(false);

    public void dfsUpDown(Node root, Node p, long top) {
        int n = root.adj.size();
        long[] ps = new long[n];
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            ps[i] = node == p ? top : node.state;
        }
        long[] ss = ps.clone();
        for (int i = 1; i < n; i++) {
            ps[i] = childAdd(ps[i - 1], ps[i]);
        }
        for (int i = n - 2; i >= 0; i--) {
            ss[i] = childAdd(ss[i], ss[i + 1]);
        }
        root.max = DigitUtils.lowBit(ss[0]);
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                continue;
            }
            long rootState = DigitUtils.asLong(1, 0);
            if(i > 0){
                rootState = fatherAdd(rootState, ps[i - 1]);
            }
            if(i + 1 < n){
                rootState = fatherAdd(rootState, ss[i + 1]);
            }
            dfsUpDown(node, root, rootState);
        }
    }

    public void dfs(Node root, Node p) {
        long state = DigitUtils.asLong(1, 0);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            state = fatherAdd(state, node.state);
        }
        root.state = state;
    }



    public long fatherAdd(long a, long b) {
        int aav = DigitUtils.highBit(a);
        int bav = DigitUtils.highBit(b);
        return DigitUtils.asLong(aav == bav ? 0 : aav, DigitUtils.lowBit(a) + DigitUtils.lowBit(b) + (aav & bav));
    }

    public long childAdd(long a, long b){
        int aav = DigitUtils.highBit(a);
        int bav = DigitUtils.highBit(b);
        return DigitUtils.asLong(aav | bav, DigitUtils.lowBit(a) + DigitUtils.lowBit(b));
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    long state;
    int max;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
