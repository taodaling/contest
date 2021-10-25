package on2021_10.on2021_10_08_AtCoder___AtCoder_Beginner_Contest_221.F___Diameter_set;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.*;

public class FDiameterSet {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null, 0);
        Node end0 = Arrays.stream(nodes).max(Comparator.comparingInt(x -> x.depth)).get();
        dfs(end0, null, 0);
        Node end1 = Arrays.stream(nodes).max(Comparator.comparingInt(x -> x.depth)).get();
        debug.debug("end0", end0);
        debug.debug("end1", end1);
        dq = new ArrayDeque<>(n);
        trace(end0, null, end1);
        List<Node> path = new ArrayList<>(dq);
        if (path.size() % 2 == 0) {
            Node mid = new Node(-1);
            Node a = path.get(path.size() / 2);
            Node b = path.get(path.size() / 2 - 1);
            a.adj.remove(b);
            b.adj.remove(a);

            mid.adj.add(a);
            mid.adj.add(b);

            path.add(path.size() / 2, mid);
        }
        debug.debug("path", path);

        Node mid = path.get(path.size() / 2);
        int half = path.size() / 2;
        long ans = 1;
        long waySum = 0;
        for (Node node : mid.adj) {
            int way = count(node, mid, 1, half);
            ans = ans * (way + 1) % mod;
            waySum += way;
            debug.debug("node", node);
            debug.debug("way", way);

        }
        ans -= waySum;
        ans--;
        ans = DigitUtils.mod(ans, mod);

        out.println(ans);
    }
    Debug debug = new Debug(false);

    public int count(Node root, Node p, int depth, int targetDepth) {
        int ans = depth == targetDepth ? 1 : 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            ans += count(node, root, depth + 1, targetDepth);
        }
        return ans;
    }

    Deque<Node> dq;

    public boolean trace(Node root, Node p, Node target) {
        dq.addLast(root);
        if (root == target) {
            return true;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (trace(node, root, target)) {
                return true;
            }
        }
        dq.removeLast();
        return false;
    }

    public void dfs(Node root, Node p, int d) {
        root.depth = d;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, d + 1);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
    int id;

    public Node(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}