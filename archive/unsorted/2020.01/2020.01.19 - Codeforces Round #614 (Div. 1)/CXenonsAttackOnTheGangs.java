package contest;

import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerDequeImpl;
import template.primitve.generated.LongDequeImpl;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.*;

public class CXenonsAttackOnTheGangs {
    Node[] nodes;
    LcaOnTree lcaOnTree;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        List<Edge> edges = new ArrayList<>(n);
        IntegerMultiWayStack stack = new IntegerMultiWayStack(n, n * 2);
        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            e.a.next.add(e);
            e.b.next.add(e);
            edges.add(e);
            stack.addLast(e.a.id, e.b.id);
            stack.addLast(e.b.id, e.a.id);
            if (e.a.id > e.b.id) {
                Node tmp = e.a;
                e.a = e.b;
                e.b = tmp;
            }
        }

        lcaOnTree = new LcaOnTree(stack, 0);
        dfsForDepth(nodes[0], null, 0);
        int[][] dist = new int[n][n];
        for(int i = 0; i < n; i++){
            for(int j = i; j < n; j++){
                dist[i][j] = dist[j][i] = dist(nodes[i], nodes[j]);
            }
        }

        for (Edge e : edges) {
            dfs(e.a, e);
            dfs(e.b, e);
            e.sizeA = e.a.size;
            e.sizeB = e.b.size;
        }

        long[][] dp = new long[n][n];
        LongDequeImpl deque = new LongDequeImpl(n * n);
        for (Edge e : edges) {
            dp[e.a.id][e.b.id] = e.sizeOf(e.a) * e.sizeOf(e.b);
            deque.addLast(dpId(e.a.id, e.b.id));
        }
        long ans = 0;
        while (!deque.isEmpty()) {
            long state = deque.removeFirst();
            int a = DigitUtils.highBit(state);
            int b = DigitUtils.lowBit(state);
            ans = Math.max(ans, dp[a][b]);
            long nextDepth = dist(nodes[a], nodes[b]) + 1;

            Edge pa = null;
            Edge pb = null;
            for (Edge e : nodes[a].next) {
                Node node = e.other(nodes[a]);
                if (dist(node, nodes[b]) != nextDepth) {
                    pa = e;
                    break;
                }
            }
            for (Edge e : nodes[b].next) {
                Node node = e.other(nodes[b]);
                if (dist(node, nodes[a]) != nextDepth) {
                    pb = e;
                    break;
                }
            }
            for (Edge e : nodes[a].next) {
                Node node = e.other(nodes[a]);
                if (e == pa) {
                    continue;
                }
                int na = node.id;
                int nb = b;
                if (na > nb) {
                    int tmp = na;
                    na = nb;
                    nb = tmp;
                }
                boolean add = dp[na][nb] == 0;
                dp[na][nb] = Math.max(dp[na][nb], dp[a][b] + e.sizeOf(node) * pb.sizeOf(nodes[b]) );
                if (add) {
                    deque.addLast(dpId(na, nb));
                }
            }
            for (Edge e : nodes[b].next) {
                Node node = e.other(nodes[b]);
                if (e == pb) {
                    continue;
                }
                int na = a;
                int nb = node.id;
                if (na > nb) {
                    int tmp = na;
                    na = nb;
                    nb = tmp;
                }
                boolean add = dp[na][nb] == 0;
                dp[na][nb] = Math.max(dp[na][nb], dp[a][b] + pa.sizeOf(nodes[a]) * e.sizeOf(node) );
                if (add) {
                    deque.addLast(dpId(na, nb));
                }
            }
        }

        //System.err.println(Arrays.deepToString(dp));

        out.println(ans);
    }

    public long dpId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public int dist(Node a, Node b) {
        int lca = lcaOnTree.lca(a.id, b.id);
        return a.depth + b.depth - 2 * nodes[lca].depth;
    }

    public void dfs(Node root, Edge p) {
        root.size = 1;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, e);
            root.size += node.size;
        }
    }

    public void dfsForDepth(Node root, Edge p, int depth) {
        root.depth = depth;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForDepth(node, e, depth + 1);
        }
    }
}


class Edge {
    Node a;
    Node b;
    int sizeA;
    int sizeB;

    long sizeOf(Node x) {
        return a == x ? sizeA : sizeB;
    }

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int size;
    int id;
    int depth;
}
