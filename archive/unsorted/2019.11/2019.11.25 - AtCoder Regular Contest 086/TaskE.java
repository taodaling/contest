package contest;



import template.datastructure.SparseTable;
import template.graph.MultiWayDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.utils.CompareUtils;

import java.util.*;

public class TaskE {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 0; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i <= n; i++) {
            Node p = nodes[in.readInt()];
            p.next.add(nodes[i]);
        }
        MultiWayDeque<Node> levels = new MultiWayDeque<>(n + 1, n + 1);
        MultiWayDeque<Node> virtualEdges = new MultiWayDeque<>(n + 1, n + 1);
        List<Node> trace = new ArrayList<>(2 * (n + 1));
        dfs(nodes[0], levels, 0, trace);
        SparseTable<Node> st = new SparseTable<>(trace.toArray(), trace.size(), (a, b) -> a.dfn <= b.dfn ? a : b);
        Deque<Node> deque = new ArrayDeque<>(n + 1);
        IntegerVersionArray va = new IntegerVersionArray(n + 1);

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            trace.clear();
            va.clear();
            for (Iterator<Node> iterator = levels.iterator(i); iterator.hasNext(); ) {
                Node node = iterator.next();
                va.set(node.id, 1);
                trace.add(node);
            }
            int cnt = trace.size();
            if(cnt == 0){
                break;
            }
            for (int j = 1, until = trace.size(); j < until; j++) {
                Node prev = trace.get(j - 1);
                Node cur = trace.get(j);
                Node lca = st.query(prev.dfn, cur.dfn);
                if (va.get(lca.id) == 1) {
                    continue;
                }
                va.set(lca.id, 1);
                trace.add(lca);
            }
            trace.sort((a, b) -> a.dfn - b.dfn);
            for (int j = 0, until = trace.size(); j < until; j++) {
                trace.get(j).virtualId = j;
            }
            deque.clear();
            virtualEdges.expandQueueNum(trace.size());
            virtualEdges.clear();
            for (Node node : trace) {
                while (!deque.isEmpty() && st.query(deque.peekLast().dfn, node.dfn) != deque.peekLast()) {
                    deque.removeLast();
                }
                if (!deque.isEmpty()) {
                    virtualEdges.addLast(deque.peekLast().virtualId, node);
                }
                deque.addLast(node);
            }

            Node root = trace.get(0);
            dpOnTree(root, virtualEdges);
            int way = root.dp[1];
            way = mod.mul(way, pow.pow(2, n + 1 - cnt));
            ans = mod.plus(ans, way);
        }
        out.println(ans);
    }

    public void dpOnTree(Node root, MultiWayDeque<Node> edges) {
        if (edges.isEmpty(root.virtualId)) {
            root.dp[0] = root.dp[1] = 1;
            return;
        }
        root.dp[0] = 1;
        root.dp[1] = 0;
        int total = 1;
        for (Iterator<Node> iterator = edges.iterator(root.virtualId); iterator.hasNext(); ) {
            Node node = iterator.next();
            dpOnTree(node, edges);
            total = mod.mul(total, mod.plus(node.dp[0], node.dp[1]));
            root.dp[1] = mod.plus(mod.mul(root.dp[1], node.dp[0]), mod.mul(root.dp[0], node.dp[1]));
            root.dp[0] = mod.mul(root.dp[0], node.dp[0]);
        }
        root.dp[0] = mod.subtract(total, root.dp[1]);
    }

    public void dfs(Node root, MultiWayDeque<Node> deque, int height, List<Node> trace) {
        deque.addLast(height, root);
        root.dfn = trace.size();
        trace.add(root);
        for (Node node : root.next) {
            dfs(node, deque, height + 1, trace);
            trace.add(root);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int virtualId;
    int[] dp = new int[2];
    int dfn;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
