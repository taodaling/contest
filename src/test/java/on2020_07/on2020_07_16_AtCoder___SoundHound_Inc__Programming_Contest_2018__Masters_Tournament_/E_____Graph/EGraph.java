package on2020_07.on2020_07_16_AtCoder___SoundHound_Inc__Programming_Contest_2018__Masters_Tournament_.E_____Graph;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.DoubleLinearFunction;
import template.math.LinearFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.w = in.readInt() - 2;
            a.adj.add(e);
            b.adj.add(e);
        }

        dfs(nodes[0], null, new DoubleLinearFunction(1, 0));
        long l = 0;
        long r = Integer.MAX_VALUE;
        for (Node node : nodes) {
            DoubleLinearFunction df = node.func;
            if (df.a == 0) {
                if (df.b < 0) {
                    valid = false;
                }
                continue;
            }
            if (df.a < 0) {
                r = Math.min(r, DigitUtils.floorDiv((long) -df.b, (long) df.a));
            } else {
                l = Math.max(l, DigitUtils.ceilDiv((long) -df.b, (long) df.a));
            }
        }

        if (l > r) {
            valid = false;
        }
        if (!valid) {
            out.println(0);
            return;
        }
        if (val != Long.MIN_VALUE) {
            if (val >= l && val <= r) {
                out.println(1);
            } else {
                out.println(0);
            }
            return;
        }

        out.println(r - l + 1);
    }

    long val = Long.MIN_VALUE;
    boolean valid = true;

    public void dfs(Node root, Edge p, DoubleLinearFunction func) {
        if (root.func != null) {
            DoubleLinearFunction delta = DoubleLinearFunction.subtract(root.func, func);
            if (delta.a == 0) {
                if (delta.b == 0) {
                    return;
                }
                valid = false;
            } else {
                if (-(long) delta.b % (long) delta.a != 0) {
                    valid = false;
                }
                if (val != Long.MIN_VALUE && -(long) delta.b / (long) delta.a != val) {
                    valid = false;
                }
                val = -(long) delta.b / (long) delta.a;
            }
            return;
        }
        root.func = func;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            dfs(e.other(root), e, DoubleLinearFunction.subtract(
                    new DoubleLinearFunction(0, e.w), root.func));
        }
    }
}


class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    DoubleLinearFunction func;
}