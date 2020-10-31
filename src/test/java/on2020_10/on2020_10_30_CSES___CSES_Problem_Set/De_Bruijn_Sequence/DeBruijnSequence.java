package on2020_10.on2020_10_30_CSES___CSES_Problem_Set.De_Bruijn_Sequence;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DeBruijnSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[1 << n - 1];
        for (int i = 0; i < 1 << n - 1; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < 1 << n; i++) {
            nodes[i >> 1].adj.add(new Edge(nodes[Bits.clear(i, n - 1)], i));
        }
        List<Edge> seq = new ArrayList<>(1 << n);
        dfs(nodes[0], null, seq);
        SequenceUtils.reverse(seq);
        boolean first = true;
        for (Edge e : seq) {
            if (first) {
                for(int j = n - 1; j >= 0; j--){
                    out.append(Bits.get(e.x, j));
                }
                first = false;
            } else {
                out.append(e.x & 1);
            }
        }
    }

    public void dfs(Node root, Edge p, List<Edge> dq) {
        while (!root.adj.isEmpty()) {
            Edge tail = root.adj.remove(root.adj.size() - 1);
            dfs(tail.to, tail, dq);
        }
        if (p != null) {
            dq.add(p);
        }
    }
}

class Edge {
    Node to;
    int x;

    public Edge(Node to, int x) {
        this.to = to;
        this.x = x;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
}