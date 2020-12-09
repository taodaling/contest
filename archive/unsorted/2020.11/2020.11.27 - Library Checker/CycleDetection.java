package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CycleDetection {
    List<DirectedEdge>[] g;

    public long eId(int a, int b) {
        return DigitUtils.asLong(a, b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        g = Graph.createGraph(n);

        Map<Long, Integer> map = new HashMap<>(m);
        for (int i = 0; i < m; i++) {
            int a = in.ri();
            int b = in.ri();
            map.put(eId(a, b), i);
            Graph.addEdge(g, a, b);
        }
        visited = new boolean[n];
        instk = new boolean[n];
        dq = new ArrayDeque<>(n);

        for (int i = 0; i < n; i++) {
            if (dfs(i)) {
                out.println(dq.size());
                for (int j = 0; j < dq.size(); j++) {
                    int a = dq.removeFirst();
                    int b = dq.peekFirst();
                    dq.addLast(a);
                    out.println(map.get(eId(a, b)));
                }
                return;
            }
        }

        out.println(-1);
    }

    boolean[] visited;
    boolean[] instk;
    Deque<Integer> dq;

    public boolean dfs(int root) {
        if (visited[root]) {
            if (instk[root]) {
                while (dq.peekFirst() != root) {
                    dq.removeFirst();
                }
                return true;
            }
            return false;
        }
        visited[root] = instk[root] = true;
        dq.addLast(root);

        for (DirectedEdge e : g[root]) {
            if (dfs(e.to)) {
                return true;
            }
        }

        dq.removeLast();
        instk[root] = false;
        return false;
    }
}
