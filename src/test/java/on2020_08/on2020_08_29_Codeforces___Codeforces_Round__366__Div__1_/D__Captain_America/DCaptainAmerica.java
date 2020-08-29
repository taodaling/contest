package on2020_08.on2020_08_29_Codeforces___Codeforces_Round__366__Div__1_.D__Captain_America;



import sun.security.x509.EDIPartyName;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.graph.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCaptainAmerica {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int r = in.readInt();
        int b = in.readInt();
        char rChar = 'r';
        char bChar = 'b';
        if (r > b) {
            int tmp = r;
            r = b;
            b = tmp;

            char cTmp = rChar;
            rChar = bChar;
            bChar = cTmp;
        }

        Edge[] edges = new Edge[n];
        Map<Integer, Node> rows = new HashMap<>();
        Map<Integer, Node> cols = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Node u = rows.computeIfAbsent(in.readInt(), x -> new Node());
            Node v = cols.computeIfAbsent(in.readInt(), x -> new Node());
            Edge e = new Edge();
            edges[i] = e;
            e.a = u;
            e.b = v;
            e.a.deg++;
            e.b.deg++;
        }

        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int l = in.readInt();
            int k = in.readInt();
            Node node;
            if (t == 1) {
                node = rows.computeIfAbsent(l, x -> new Node());
            } else {
                node = cols.computeIfAbsent(l, x -> new Node());
            }
            intersect(node.lr, bounds(node.deg, k));
            if (node.lr[0] > node.lr[1]) {
                out.println(-1);
                return;
            }
        }

        int idAlloc = 0;
        for (Node u : rows.values()) {
            u.id = idAlloc++;
        }
        for (Node v : cols.values()) {
            v.id = idAlloc++;
        }
        int src = idAlloc++;
        int dst = idAlloc++;
        List<IntegerLRFlowEdge>[] g = IntegerFlow.createLRFlow(dst + 1);

        for (Edge e : edges) {
            e.e = IntegerFlow.addLREdge(g, e.a.id, e.b.id, 1, 0);
        }

        for (Node node : rows.values()) {
            IntegerFlow.addLREdge(g, src, node.id, node.lr[1], node.lr[0]);
        }
        for (Node node : cols.values()) {
            IntegerFlow.addLREdge(g, node.id, dst, node.lr[1], node.lr[0]);
        }

        IntegerMaximumFlow flow = new IntegerDinic();
        boolean feasible = IntegerFlow.feasibleFlow(g, src, dst, flow);
        if (!feasible) {
            out.println(-1);
            return;
        }
        flow.apply((List[]) g, src, dst, (int) 1e9);
        long ans = 0;
        for (Edge e : edges) {
            if (e.e.flow == 1) {
                ans += r;
            } else {
                ans += b;
            }
        }

        out.println(ans);
        for (Edge e : edges) {
            if (e.e.flow == 1) {
                out.append(rChar);
            } else {
                out.append(bChar);
            }
        }
    }

    public void intersect(int[] a, int[] b) {
        a[0] = Math.max(a[0], b[0]);
        a[1] = Math.min(a[1], b[1]);
    }

    public int[] bounds(int n, int k) {
        //x-(n-x)<=k
        //(n-x)-x<=k
        //2x<=n+k
        //2x>=n-k
        return new int[]{DigitUtils.ceilDiv(n - k, 2),
                DigitUtils.floorDiv(n + k, 2)};
    }
}

class Edge {
    Node a;
    Node b;
    IntegerFlowEdge e;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int id;
    int[] lr = new int[]{0, (int) 1e8};
    IntegerFlowEdge edge;
    int deg;
}
