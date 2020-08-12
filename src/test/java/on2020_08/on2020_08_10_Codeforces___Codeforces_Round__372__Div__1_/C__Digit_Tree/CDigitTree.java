package on2020_08.on2020_08_10_Codeforces___Codeforces_Round__372__Div__1_.C__Digit_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CDigitTree {

    Modular mod;
    int[] pow;
    int[] inv;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        mod = new Modular(m);
        pow = new int[n + 1];
        inv = new int[n + 1];
        pow[0] = inv[0] = 1;
        pow[1] = mod.valueOf(10);
        inv[1] = new Power(mod).inverse(pow[1]);
        for (int i = 2; i <= n; i++) {
            pow[i] = mod.mul(pow[i - 1], pow[1]);
            inv[i] = mod.mul(inv[i - 1], inv[1]);
        }


        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.digit = in.readInt();
            a.adj.add(e);
            b.adj.add(e);
        }

        dac(nodes[0]);
        out.println(cnt);
    }

    public void findSize(Node root, Edge p) {
        root.size = 1;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            findSize(node, e);
            root.size += node.size;
        }
    }

    public Node findCentroid(Node root, Edge p, int total) {
        int max = total - root.size;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            Node ans = findCentroid(node, e, total);
            if (ans != null) {
                return ans;
            }
            max = Math.max(max, node.size);
        }
        if (max * 2 <= total) {
            return root;
        }
        return null;
    }

    long cnt = 0;
    IntegerHashMap hm = new IntegerHashMap((int) 1e5, false);
    List<Node> trace = new ArrayList<>((int) 1e5);

    public void trace(Node root, Edge p) {
        Node top = p.other(root);
        root.depth = top.depth + 1;
        root.down = mod.mul(top.down, 10);
        root.down = mod.plus(root.down, p.digit);
        root.up = mod.mul(p.digit, pow[root.depth - 1]);
        root.up = mod.plus(root.up, top.up);

        trace.add(root);
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            trace(node, e);
        }
    }

    public void dac(Node root) {
        if (root.adj.isEmpty()) {
            return;
        }
        findSize(root, null);
        root = findCentroid(root, null, root.size);
        root.up = root.down = 0;
        root.depth = 0;
        hm.clear();
        hm.put(root.up, 1);
        for (Edge e : root.adj) {
            trace.clear();
            trace(e.other(root), e);
            //sort then map
            for (Node node : trace) {
                node.down = mod.mul(node.down, inv[node.depth]);
                cnt += hm.getOrDefault(mod.valueOf(-node.down), 0);
            }
            for (Node node : trace) {
                hm.put(node.up, hm.getOrDefault(node.up, 0) + 1);
            }
        }

        SequenceUtils.reverse(root.adj);
        hm.clear();
        for (Edge e : root.adj) {
            trace.clear();
            trace(e.other(root), e);
            //sort then map
            for (Node node : trace) {
                node.down = mod.mul(node.down, inv[node.depth]);
                cnt += hm.getOrDefault(mod.valueOf(-node.down), 0);
            }
            for (Node node : trace) {
                hm.put(node.up, hm.getOrDefault(node.up, 0) + 1);
            }
        }
        cnt += hm.getOrDefault(mod.valueOf(-root.down), 0);

        for (Edge e : root.adj) {
            Node node = e.other(root);
            node.adj.remove(e);
            dac(node);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int digit;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int id;
    List<Edge> adj = new ArrayList<>();
    int size;
    int up;
    int down;
    int depth;

    static Comparator<Node> sortById = (a, b) -> Integer.compare(a.id, b.id);

    @Override
    public String toString() {
        return "" + (id);
    }
}